teen_preg_list = ["rgb(247,251,255)", "rgb(222,235,247)", "rgb(198,219,239)", "rgb(158,202,225)", "rgb(107,174,214)", "rgb(66,146,198)", "rgb(33,113,181)", "rgb(8,81,156)", "rgb(8,48,107)"]
chlamydia_list = ["rgb(200,229,109)", "rgb(189,224,80)", "rgb(179,219,51)", "rgb(161,200,36)", "rgb(138,171,31)", "rgb(114,142,25)", "rgb(91,114,20)", "rgb(68,85,15)", "rgb(45,56,10)"]
uninsured_list = ["rgb(237,187,153)", "rgb(232,168,124)", "rgb(227,149,95)", "rgb(222,130,66)", "rgb(216,111,38)", "rgb(187,96,33)", "rgb(158,81,28)", "rgb(129,66,23)", "rgb(100,51,18)"]
poverty_list = ["rgb(200,252,255)", "rgb(132,247,255)", "rgb(13,240,255)", "rgb(0,219,234)", "rgb(0,188,200)", "rgb(0,156,166)", "rgb(0,124,132)", "rgb(0,92,98)", "rgb(0,60,64)"]
adherents_list = ["rgb(190,109,229)", "rgb(177,80,224)", "rgb(164,51,219)", "rgb(147,36,200)", "rgb(125,31,171)", "rgb(104,25,142)", "rgb(83,20,114)", "rgb(62,15,85)", "rgb(41,10,56)"]
max_risk_list = ["rgb(220,220,220)", "rgb(220,220,220)", "rgb(220,220,220)", "rgb(220,220,220)", "rgb(220,220,220)", "rgb(220,220,220)", "rgb(220,220,220)", "rgb(220,220,220)", "rgb(120,32,21)"]


### Bins a dataframe series into n bins
def pct_rank_qcut(series, n):
    edges = pd.Series([float(i) / n for i in range(n + 1)])
    f = lambda x: (edges >= x).argmax()
    return series.rank(pct=1).apply(f)


for i in column_list:
    column_name = str(i) + '_index'
    df_colors[column_name] = pct_rank_qcut(df_colors[i], 9)
	
for j in index_list:
    df_colors[j] = df_colors[j].map(lambda x: x - 1)
	
df_colors['preg_rate_color'] = df_colors['preg_rate_index'].map(lambda x: teen_preg_list[x])
