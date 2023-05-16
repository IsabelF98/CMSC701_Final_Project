#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This file runs pa-bech on the synthetic data and records the average run time over
100 runs. The run times are then saved as csv files.

WARNING: This file takes a long time to run (around 10 hours)
"""

import os
import json
import numpy as np
import pandas as pd

# Working directory
DIR = " "

# Lengths of reads:
n_vals = [100, 150, 200, 250]
# Error rate
e_vals = [0.05, 0.25, 0.5, 0.75]
# Substitution vs. Deletion ratio
s_vals = [0.95, 0.75, 0.25, 0.05]

N = 100 # number of runs

astar_data_df = pd.DataFrame(index=n_vals)
edlib_data_df = pd.DataFrame(index=n_vals)
wfa_data_df   = pd.DataFrame(index=n_vals)

for e in e_vals:
    for s in s_vals:
        astar_avg_time_arr = np.zeros(len(n_vals))
        edlib_avg_time_arr = np.zeros(len(n_vals))
        wfa_avg_time_arr   = np.zeros(len(n_vals))
        for k in range(len(n_vals)):
            n = n_vals[k]
            astar_time_arr = np.zeros(N)
            edlib_time_arr = np.zeros(N)
            wfa_time_arr   = np.zeros(N)
            for i in range(N):
                # Run pa-bench
                file = "synth_n"+str(n)+"_e"+str(int(e*100))+"_s"+str(int(s*100))
                run = "cargo run --release -- --rerun-all --no-pin ../evals/"+file+".yaml" # pa-bech call
                os.system("cd "+DIR+"benchmarking/pa-bench ; "+run)
                
                # Get time
                data_file = open(DIR+"benchmarking/evals/"+file+".json")
                data = json.load(data_file)
                data_file.close()
                for j in [0,1,2]:
                    alg = list(data[j]['job']['algo'].keys())[0]
                    if alg == 'AstarPA':
                        astar_time_arr[i] = data[j]['output']['Ok']['measured']['runtime']
                    elif alg == 'Edlib':
                        edlib_time_arr[i] = data[j]['output']['Ok']['measured']['runtime']
                    elif alg == 'Wfa':
                        wfa_time_arr[i] = data[j]['output']['Ok']['measured']['runtime']
            
            # Find average time
            astar_avg_time_arr[k] = np.average(astar_time_arr)
            edlib_avg_time_arr[k] = np.average(edlib_time_arr)
            wfa_avg_time_arr[k]   = np.average(wfa_time_arr)
        
        astar_data_df["e="+str(e)+",s="+str(s)] = astar_avg_time_arr
        edlib_data_df["e="+str(e)+",s="+str(s)] = edlib_avg_time_arr
        wfa_data_df["e="+str(e)+",s="+str(s)]   = wfa_avg_time_arr

#save data to csv file
astar_data_df.to_csv('synth_astar_time.csv')
edlib_data_df.to_csv('synth_edlib_time.csv') 
wfa_data_df.to_csv('synth_wfa_time.csv') 
