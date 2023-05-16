# Benchmarking

The code in this file are used to evaluate runtime of the short read alignment algoritms.

## File Requierments
***
To run the files in this directory it is expected that you have the following set up:
* [Pairwise Alignment Benchmarks](https://github.com/pairwise-alignment/pa-bench) installed and built in this directory as `pa-bench`.
* The data to run as `seq` files (see conversion function in `data-processing`) located in `pa-bench/evals/data/manual`.
* `yaml` files for running `pa-bench`. See files in `evals` folder for details.

## Files located in the directory
***
To run all Python files simply run them on your favorite IDE.
* `time_synth_data_pa_bench.py`: This file runs pa-bech on the synthetic data and records the average run time over 100 runs. The average run times are then saved as csv files in the directory.
* `time_synth_data_minimap.pu`: This file creates creates a data frame of the average runtimes using Minimap2 as csv file. NOTE: The times are hard coded into the file. We were unable to figure out how to extract the runtime from the minimap output. These times are what was stated on the comand line output after each run. See `minimap2` directory for instructions on running Minimap2.
* `evals/synth_write_yaml.py`: This file creates the yaml files needed to run pa-bench on the synthetic data. NOTE: These yaml files will not run on pa-bench without some slight altercations done by hand. See test.yaml for exact formating.
* `plots.py`: This file creates the runtime plots for the different algorithms. NOTE: Plots are not automatically saved.