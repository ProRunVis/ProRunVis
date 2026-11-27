#!/bin/bash
# Run the full evaluation pipeline for all generated benchmarks.
#
# For each benchmark:
#   1. Instrument with SrcTracer
#   2. Compile + run with text runtime  → .trace.txt
#   3. Compile + run with binary runtime → .trace
#   4. Record trace file sizes
#   5. Prepare ProRunVis upload folders (original source + trace)
#
# After running this script, start ProRunVis and upload each folder from
# evaluation/prorunvis-input/<benchmark>/ to measure processing times.
#
# Usage: ./run-evaluation.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRCTRACER="/Users/salma/Java_Src_tracer/java-src-tracer"
GENERATED="$SCRIPT_DIR/generated"
PRORUNVIS_INPUT="$SCRIPT_DIR/prorunvis-input"
RESULTS="$SCRIPT_DIR/results.csv"

INSTRUMENTER="$SRCTRACER/instrumenter/build/libs/instrumenter-0.1.0-SNAPSHOT-all.jar"
RUNTIME_TEXT="$SRCTRACER/runtime/build/libs/runtime-0.1.0-SNAPSHOT.jar"
RUNTIME_BIN="$SRCTRACER/runtime-binary/build/libs/runtime-binary-0.1.0-SNAPSHOT.jar"

# Check prerequisites
if [ ! -f "$INSTRUMENTER" ]; then
    echo "ERROR: SrcTracer not built. Run: cd $SRCTRACER && ./gradlew build"
    exit 1
fi

echo "benchmark,java_files,trace_text_size_bytes,trace_binary_size_bytes,trace_text_tokens" > "$RESULTS"

for bench_dir in "$GENERATED"/*/; do
    bench_name=$(basename "$bench_dir")
    echo ""
    echo "=== $bench_name ==="

    # Count Java files
    java_count=$(find "$bench_dir" -name "*.java" | wc -l | tr -d ' ')

    # Create working directory
    work_dir="$SCRIPT_DIR/work/$bench_name"
    rm -rf "$work_dir"
    mkdir -p "$work_dir/instrumented"

    # Step 1: Instrument all Java files
    echo "  Instrumenting..."
    for src in "$bench_dir"*.java; do
        fname=$(basename "$src")
        java -jar "$INSTRUMENTER" "$src" "$work_dir/instrumented/$fname" 2>/dev/null || {
            echo "  WARNING: Failed to instrument $fname, skipping benchmark"
            continue 2
        }
    done

    # Step 2: Compile + run with TEXT runtime
    echo "  Running (text mode)..."
    rm -rf "$work_dir/trace-out"
    mkdir -p "$work_dir/trace-out"
    cd "$work_dir"
    javac -cp "$RUNTIME_TEXT" instrumented/*.java 2>/dev/null
    # Find the class with main (Main.java or the single class)
    main_class=""
    if [ -f "$bench_dir/Main.java" ]; then
        main_class="Main"
    else
        # Use the first (or only) Java file's class name
        main_class=$(basename "$(ls "$bench_dir"*.java | head -1)" .java)
    fi
    java -cp "instrumented:$RUNTIME_TEXT" "$main_class" 2>/dev/null || true
    cd "$SCRIPT_DIR"

    # Find the generated text trace
    text_trace=$(find "$work_dir/trace-out" -name "*.trace.txt" | head -1)
    text_size=0
    text_tokens=0
    if [ -n "$text_trace" ] && [ -f "$text_trace" ]; then
        text_size=$(wc -c < "$text_trace" | tr -d ' ')
        text_tokens=$(wc -c < "$text_trace" | tr -d ' ')
        echo "  Text trace: $text_size bytes"
    else
        echo "  WARNING: No text trace produced"
    fi

    # Step 3: Compile + run with BINARY runtime
    echo "  Running (binary mode)..."
    rm -rf "$work_dir/trace-out"
    mkdir -p "$work_dir/trace-out"
    cd "$work_dir"
    javac -cp "$RUNTIME_BIN" instrumented/*.java 2>/dev/null
    java -cp "instrumented:$RUNTIME_BIN" "$main_class" 2>/dev/null || true
    cd "$SCRIPT_DIR"

    # Find the generated binary trace
    bin_trace=$(find "$work_dir/trace-out" -name "*.trace" ! -name "*.trace.txt" | head -1)
    bin_size=0
    if [ -n "$bin_trace" ] && [ -f "$bin_trace" ]; then
        bin_size=$(wc -c < "$bin_trace" | tr -d ' ')
        echo "  Binary trace: $bin_size bytes"
    else
        echo "  WARNING: No binary trace produced"
    fi

    # Step 4: Prepare ProRunVis upload folders (one with text, one with binary)
    echo "  Preparing ProRunVis input folders..."

    # Text trace folder
    pv_text="$PRORUNVIS_INPUT/${bench_name}_text"
    rm -rf "$pv_text"
    mkdir -p "$pv_text"
    cp "$bench_dir"*.java "$pv_text/"
    if [ -n "$text_trace" ] && [ -f "$text_trace" ]; then
        # Re-generate text trace (was overwritten by binary run)
        cd "$work_dir"
        rm -rf trace-out && mkdir trace-out
        javac -cp "$RUNTIME_TEXT" instrumented/*.java 2>/dev/null
        java -cp "instrumented:$RUNTIME_TEXT" "$main_class" 2>/dev/null || true
        cd "$SCRIPT_DIR"
        new_text_trace=$(find "$work_dir/trace-out" -name "*.trace.txt" | head -1)
        if [ -n "$new_text_trace" ]; then
            cp "$new_text_trace" "$pv_text/"
        fi
    fi

    # Binary trace folder
    pv_bin="$PRORUNVIS_INPUT/${bench_name}_binary"
    rm -rf "$pv_bin"
    mkdir -p "$pv_bin"
    cp "$bench_dir"*.java "$pv_bin/"
    if [ -n "$bin_trace" ] && [ -f "$bin_trace" ]; then
        # Re-generate binary trace
        cd "$work_dir"
        rm -rf trace-out && mkdir trace-out
        javac -cp "$RUNTIME_BIN" instrumented/*.java 2>/dev/null
        java -cp "instrumented:$RUNTIME_BIN" "$main_class" 2>/dev/null || true
        cd "$SCRIPT_DIR"
        new_bin_trace=$(find "$work_dir/trace-out" -name "*.trace" ! -name "*.trace.txt" | head -1)
        if [ -n "$new_bin_trace" ]; then
            cp "$new_bin_trace" "$pv_bin/"
        fi
    fi

    # Record results
    echo "$bench_name,$java_count,$text_size,$bin_size,$text_tokens" >> "$RESULTS"
done

echo ""
echo "=== Done ==="
echo "Results:          $RESULTS"
echo "ProRunVis inputs: $PRORUNVIS_INPUT/"
echo ""
echo "Next steps:"
echo "  1. Start ProRunVis: cd /Users/salma/ProRunVis && ./gradlew clean prorunvis-api:bootJar && java -jar prorunvis-api/build/libs/prorunvis-api.jar"
echo "  2. Upload each folder from $PRORUNVIS_INPUT/ via the web UI"
echo "  3. Read timing from the server console output"
echo "  4. Record results in the evaluation table"
