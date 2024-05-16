from flask import jsonify
import yfinance as yf
import requests
from requests_html import HTMLSession
import pandas as pd
import unidecode

def remove_accents(a):
    return unidecode.unidecode(a)

def to_str(x):
    return str(x)

def get_value_data(symbol):
    historic = yf.download(symbol, period="1d", interval="1h")
    price = historic.loc[:,"Close"]
    print(price)
    return price.apply(to_str).to_json(date_format='iso')

def get_crypto_symbols():
    session = HTMLSession()
    num_currencies=250
    resp = session.get(f"https://finance.yahoo.com/crypto?offset=0&count={num_currencies}")
    tables = pd.read_html(resp.html.raw_html)
    df = tables[0].copy()
    df2 = df[['Symbol', 'Name']]
    df2['Name'] = df2['Name'].apply(remove_accents)
    results = df2.drop_duplicates(subset='Name', keep='first').set_index('Name').to_json()
    return results



def get_stock_symbols():    
    session = HTMLSession()
    resp = session.get(f"https://finance.yahoo.com/quote/%5EIBEX/components")
    tables = pd.read_html(resp.html.raw_html)             
    df = tables[0].copy()
    df1 = df[['Symbol', 'Company Name']]
    df1['Company Name'] = df1['Company Name'].apply(remove_accents)
    results = df1.set_index('Company Name').to_json()
    symbols_yf = df.Symbol.tolist()
    resp = session.get(f"https://finance.yahoo.com/screener/predefined/most_actives/?offset=0&count=100")
    tables = pd.read_html(resp.html.raw_html)             
    df = tables[0].copy()
    df2 = df[['Symbol', 'Name']]
    df2['Name'] = df2['Name'].apply(remove_accents)
    results2 = df2.drop_duplicates(subset='Name', keep='first').set_index('Name').to_json()
    results += ","
    results += results2.removeprefix('{"Symbol":{')
    results = results.replace("}},", ',')
    return results

print(get_crypto_symbols())



