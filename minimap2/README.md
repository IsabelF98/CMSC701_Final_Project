# Minimap 2

The code in this file are used to run Minimap2.

## File Requierments
***
To run the files in this directory it is expected that you have the following set up:
* [Minimap2](https://github.com/lh3/minimap2) installed and built as this directory.
* The data as a refrence and reads `fasta` file located in `test/synthetic_data` with the header updated.

## Files located in the directory
***
To run all Python files simply run them on your favorite IDE.
* `synth_write_minimap.py`: This file creates a bash script for running minimap on the synthetic data Header must be updated first.
* `test/synthetic_data/change_fasta_header.py`: This file changes the header created in `data_processing` so it is comatible with minimap set-up.