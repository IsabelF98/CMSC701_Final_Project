#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
This file writes a bash script for running minimap on the synthetic data.
Header must be updated first.
"""


# Lengths of reads:
n_vals = [100, 150, 200, 250]
# Error rate
e_vals = [0.05, 0.25, 0.5, 0.75]
# Substitution vs. Deletion ratio
s_vals = [0.95, 0.75, 0.25, 0.05]

fname = 'synth_run_minimap.sh'

with open(fname, "w") as f:
        f.write("#! /bin/bash\n")
        for n in n_vals:
            for e in e_vals:
                for s in s_vals:
                    # refrence file name
                    ref_fn = "ref_n"+str(n)+"_e"+str(int(e*100))+"_s"+str(int(s*100))+".fasta"
                    # reads file name (with updated header)
                    reads_fn = "reads_n"+str(n)+"_e"+str(int(e*100))+"_s"+str(int(s*100))+"_copy.fasta"
                    # output file name
                    out_fn = "out_n"+str(n)+"_e"+str(int(e*100))+"_s"+str(int(s*100))+".sam"
                    # minimap execution line
                    line = "./minimap2 -ax sr test/synthetic_data/"+ref_fn+" test/synthetic_data/"+reads_fn+" > "+out_fn+"\n"
                    f.write(line)
                    f.write("echo \n")
                    
