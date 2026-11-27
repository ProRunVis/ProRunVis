#!/bin/bash
# Upload each benchmark folder to ProRunVis via its HTTP API and capture timing.
#
# Requires ProRunVis server running on localhost:8080.
# Usage: ./run-prorunvis-benchmarks.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
INPUT_DIR="$SCRIPT_DIR/prorunvis-input"
RESOURCES_IN="/Users/salma/ProRunVis/prorunvis-api/resources/in"

BENCHMARKS=(
    trace_len_10
    trace_len_100
    trace_len_1000
    trace_len_10000
    trace_len_50000
    nesting_5
    nesting_10
    nesting_20
    nesting_50
    files_1
    files_5
    files_10
    files_20
    combined_large
)

for bench in "${BENCHMARKS[@]}"; do
    for fmt in text binary; do
        folder="$INPUT_DIR/${bench}_${fmt}"
        if [ ! -d "$folder" ]; then
            echo "SKIP: $folder not found"
            continue
        fi

        echo ""
        echo "=== $bench ($fmt) ==="

        # Clear previous upload by wiping resources/in
        rm -rf "$RESOURCES_IN"/*

        # Upload all files
        upload_args=""
        for f in "$folder"/*; do
            fname=$(basename "$f")
            upload_args="$upload_args -F file=@$f;filename=${bench}_${fmt}/$fname"
        done

        http_upload=$(curl -s -o /dev/null -w "%{http_code}" \
            http://localhost:8080/api/upload \
            -X POST \
            $upload_args)
        echo "  Upload: HTTP $http_upload"

        if [ "$http_upload" != "200" ]; then
            echo "  FAILED upload"
            continue
        fi

        # Trigger processing (allow long time for large traces)
        http_process=$(curl -s -o /dev/null -w "%{http_code}" \
            --max-time 300 \
            http://localhost:8080/api/process)
        echo "  Process: HTTP $http_process"

        sleep 0.3
    done
done

echo ""
echo "=== All done ==="
echo "Check server log for timing data."
