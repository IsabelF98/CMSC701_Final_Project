#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This file creates the yaml files needed to run pa-bench on the synthetic data.

NOTE: These yaml files will not run on pa-bench without some slight altercations
done by hand. See test.yaml for exact formating.
"""

import yaml

# Lengths of reads:
n_vals = [100, 150, 200, 250]
# Error rate
e_vals = [0.05, 0.25, 0.5, 0.75]
# Substitution vs. Deletion ratio
s_vals = [0.95, 0.75, 0.25, 0.05]

# Use sample.yaml template
with open('sample.yaml', 'r') as file:
    sample = yaml.load(file, Loader=yaml.FullLoader)

# Write yaml fiel for data
for n in n_vals:
    for e in e_vals:
        for s in s_vals:
            reads_seq = "reads_n"+str(n)+"_e"+str(int(e*100))+"_s"+str(int(s*100))+".seq"
            sample[0]['datasets'] = "!File manual/"+reads_seq
            reads_yaml = "synth_n"+str(n)+"_e"+str(int(e*100))+"_s"+str(int(s*100))+".yaml"
            with open(reads_yaml, 'w') as file:
                yaml.dump(sample, file)

