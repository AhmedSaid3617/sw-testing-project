#!/bin/bash

# Script to convert proj.md to PDF using Pandoc
# Run this script from the docs directory

pandoc TESTING_REPORT.md \
  -o "phase2_report.pdf" \
  --pdf-engine=xelatex \
  --toc \
  --toc-depth=3 \
  --number-sections \
  --highlight-style=tango \
  -V geometry:margin=1in \
  -V fontsize=12pt \
  -V papersize=a4 \
  -V colorlinks=true \
  -V linkcolor=blue \
  -V urlcolor=blue \
  -V toccolor=black

echo "PDF generated successfully: Online_Carpooling_System_Report.pdf"
