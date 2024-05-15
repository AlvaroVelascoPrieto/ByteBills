
def parse_date(date):
    date_month = str(date).split('/')[1]
    date_month = int(date_month) + 1
    splitted_date = date.split('/')
    final_date = splitted_date[0] + '-' + str(date_month) + '-' + splitted_date[2]

    return final_date

def parse_endpoint_user_stock(data):
    return data.split('&')

    