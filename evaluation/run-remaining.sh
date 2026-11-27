#!/bin/bash
# Run only the nesting/files/combined benchmarks (small traces, should complete quickly)
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
INPUT_DIR="$SCRIPT_DIR/prorunvis-input"
RESOURCES_IN="/Users/salma/ProRunVis/prorunvis-api/resources/in"

BENCHMARKS=(
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
        [ ! -d "$folder" ] && continue

        echo ""
        echo "=== $bench ($fmt) ==="

        find "$RESOURCES_IN" -mindepth 1 -delete 2>/dev/null; true

        upload_args=""
        for f in "$folder"/*; do
            fname=$(basename "$f")
            upload_args="$upload_args -F file=@$f;filename=${bench}_${fmt}/$fname"
        done

        curl -s -o /dev/null -w "  Upload: HTTP %{http_code}\n" \
            http://localhost:8080/api/upload -X POST $upload_args

        curl -s -o /dev/null -w "  Process: HTTP %{http_code}\n" \
            --max-time 300 http://localhost:8080/api/process

        sleep 0.3
    done
done

echo ""
echo "=== Done ==="
