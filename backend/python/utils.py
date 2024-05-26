
def parse_date(date):
    date_month = str(date).split('/')[1]
    date_month = int(date_month) + 1
    splitted_date = date.split('/')
    final_date = splitted_date[0] + '-' + str(date_month) + '-' + splitted_date[2]

    return final_date

def parse_endpoint_user_stock(data):
    return data.split('&')

def calculate_statistics(data):
    # data is an array with the following format:
    # [username, email, [price1, qty1, stocksym1], [price2, qty2, stocksym2], ...]
    username = data[0]
    email = data[1]
    assets = {}
    total_value = 0
    asset_count = 0

    for list in data[2:]:
        price = list[0]
        qty = list[1]
        stock_symbol = list[2]
        value = price * qty
        total_value += value
        asset_count += qty
        if stock_symbol in assets:
            assets[stock_symbol]['value'] += value
            assets[stock_symbol]['qty'] += qty
        else:
            assets[stock_symbol] = {'value': value, 'qty': qty}

    # Sort the assets by value in descending order and take the top 3
    top_3_assets = sorted(assets.items(), key=lambda x: x[1]['value'], reverse=True)[:3]
    # Calculate average value of assets
    avg_value = round(total_value / asset_count) if asset_count > 0 else 0
    # Find the most common asset
    most_common_asset = max(assets.items(), key=lambda x: x[1]['qty']) if assets else ['Not enough assets', {"qty": 0, "value": 'N/A'}]

    if len(top_3_assets) < 3:
        return {
            'username': username,
            'email': email,
            'top_3_assets': [
                ['Not enough assets', {'qty': 0, 'value': 'N/A'}],
                ['Not enough assets', {'qty': 0, 'value': 'N/A'}],
                ['Not enough assets', {'qty': 0, 'value': 'N/A'}]
            ],
            'total_value': total_value,
            'avg_value': avg_value,
            'asset_count': len(assets),
            'most_common_asset': most_common_asset
        }

    return {
        'username': username,
        'email': email,
        'top_3_assets': top_3_assets,
        'total_value': total_value,
        'avg_value': avg_value,
        'asset_count': len(assets),
        'most_common_asset': most_common_asset
    }