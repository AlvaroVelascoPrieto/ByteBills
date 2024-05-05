import yfinance as yf
import requests
from requests_html import HTMLSession
import pandas as pd


def get_value_data(symbol):
    historic = yf.download(symbol, period="1d", interval="1h")
    price = historic.loc[:,"Close"]
    print(price)
    return price.to_json(date_format='iso')

def get_crypto_symbols():    
    session = HTMLSession()
    num_currencies=250
    resp = session.get(f"https://finance.yahoo.com/crypto?offset=0&count={num_currencies}")
    tables = pd.read_html(resp.html.raw_html)               
    df = tables[0].copy()
    symbols_yf = df.Symbol.tolist()
    print(symbols_yf[:15])
    return symbols_yf