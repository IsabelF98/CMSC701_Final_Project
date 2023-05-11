# Data Processing  

Code to handle real data and generate synthetic data.  
</br>  

---------

</br>   

## generate_data.py  

</br>

Code to generate synthetic data.  

</br>  

Call from command line:  

    python generate_data.py ref_filename outfile length n_reads

</br>

Required Parameters (in order):  

- **ref_filename**: string. Path to fasta file in which to save generated reference sequence  

- **outfile**: string. Path to fasta file in which to save generated reads  

- **length**: int. Length of string to create (for both reference and reads)  

- **n_reads**: int. Number of reads to create  

</br>


Optional parameters:  

- **"-e", "--error_rate"**: float. Error rate. Reads will have length * e errors (occuring at uniformly random indices).  

- **"-s", "--sub_rate"**: float. Rate at which errors are substitutions.

- **"-d", "--del_rate"**: float. Rate at which errors are deletions.

- **"-i", "--ins_rate"**: float. Rate at which errors are insertions.  


</br>
</br>

----------



</br>   

## subselect_data.py  

</br>

Code to handle real data. Specifically, extracts reads that are of a length in the input range from a file containing many reads.   

</br>  

Call from command line:  

    python subselect_data.py file outfile length n_reads

</br>

Required Parameters (in order):  

- **file**: string. Path to fasta file to read

- **outfile**: string. Path to fasta file in which to save selected reads  

- **start**: int. Start of range of read lengths. Only reads of length $\geq$ **start** will be selected   

- **end**: int. End of range of read lengths. Only reads of length $\leq$ **end** will be selected



</br>
</br>

----------



</br>   

## convert_file.py    

</br>

Code to convert fasta to sequence file for pa-bench.  

</br>

Call from command line:  

    python generate_data.py ref_filename reads_filename  


Required Parameters (in order):  

- **ref_filename**: string. Path to fasta file of reference to use for restructuring reads data  

- **reads_filename**: string. Path to fasta file containing reads to restructure  


</br>


Outputs a file of the same name as **reads_filename** but with extension .seq instead of .fasta.  


</br>
</br>

----------



</br>   

## make_bash_scripty.py    

</br>

Code to make a bash script that, when run, generates the synthetic data with the specified parameters.  

