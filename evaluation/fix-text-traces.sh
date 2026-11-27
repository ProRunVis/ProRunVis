#!/bin/bash
# Regenerate text traces into the prorunvis-input folders that are missing them.

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRCTRACER="/Users/salma/Java_Src_tracer/java-src-tracer"
RUNTIME_TEXT="$SRCTRACER/runtime/build/libs/runtime-0.1.0-SNAPSHOT.jar"
INPUT_DIR="$SCRIPT_DIR/prorunvis-input"

for text_dir in "$INPUT_DIR"/*_text; do
    bench=$(basename "$text_dir" _text)

    # Check if trace already exists
    if find "$text_dir" -name "*.trace.txt" | grep -q .; then
        echo "SKIP $bench: trace.txt already present"
        continue
    fi

    work="$SCRIPT_DIR/work/$bench"
    if [ ! -d "$work/instrumented" ]; then
        echo "SKIP $bench: no instrumented code found"
        continue
    fi

    echo "Regenerating text trace for $bench..."
    cd "$work"
    rm -rf trace-out && mkdir -p trace-out

    # Determine main class
    gen_dir="$SCRIPT_DIR/generated/$bench"
    if [ -f "$gen_dir/Main.java" ]; then
        main_class="Main"
    else
        main_class=$(basename "$(ls "$gen_dir"/*.java | head -1)" .java)
    fi

    javac -cp "$RUNTIME_TEXT" instrumented/*.java 2>/dev/null
    java -cp "instrumented:$RUNTIME_TEXT" "$main_class" 2>/dev/null || true

    trace_file=$(find trace-out -name "*.trace.txt" | head -1)
    if [ -n "$trace_file" ]; then
        cp "$trace_file" "$text_dir/"
        echo "  OK: copied $(basename "$trace_file") -> $text_dir/"
    else
        echo "  FAIL: no trace.txt produced"
    fi
done
