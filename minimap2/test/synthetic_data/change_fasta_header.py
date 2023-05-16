#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon May 15 14:02:57 2023

@author: Isabel
"""

# Lengths of reads:
n_vals = [100, 150, 200, 250]
# Error rate
e_vals = [0.05, 0.25, 0.5, 0.75]
# Substitution vs. Deletion ratio
s_vals = [0.95, 0.75, 0.25, 0.05]

for n in n_vals:
    for e in e_vals:
        for s in s_vals:
            reads_fn = "reads_n"+str(n)+"_e"+str(int(e*100))+"_s"+str(int(s*100))+".fasta"
            
            loc = reads_fn.find('.')
            filename = reads_fn[:loc] + "_copy.fasta"
            
            with open(reads_fn, "r") as f:
                lines = f.readlines()
                
            with open(filename, "w") as f:
                i = 0
                for line in lines:
                    if "header" in line:
                        continue
                    f.write(">header" + str(i) + "\n")
                    f.write(line)
                    i += 1
        