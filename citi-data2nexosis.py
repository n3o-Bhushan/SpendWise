import numpy as np



allData = {
  "creditCardAccountTransactions": [
    {
      "accountId": "2016764541",
      "displayAccountNumber": "XXXXXXXXXXXX0378",
      "transactionDate": "2017-02-26",
      "transactionDescription": "DAN SUNG SA",
      "transactionAmount": 31,
      "eligibleForEqualPaymentPlan": "NOT_ELIGIBLE",
      "runningBalanceAmount": 610.41,
      "currencyCode": "USD",
      "transactionStatus": "PENDING",
      "merchantCategory": "5812",
      "merchantDescription": "Eating Places and Restaurants",
      "transactionType": "PURCHASE"
    },
    {
      "accountId": "2016764541",
      "displayAccountNumber": "XXXXXXXXXXXX0378",
      "transactionDate": "2017-02-26",
      "transactionDescription": "KROGER #0577",
      "transactionAmount": 14.57,
      "eligibleForEqualPaymentPlan": "NOT_ELIGIBLE",
      "runningBalanceAmount": 579.41,
      "currencyCode": "USD",
      "transactionStatus": "PENDING",
      "merchantCategory": "5411",
      "merchantDescription": "Grocery Stores, Supermarkets",
      "transactionType": "PURCHASE"
    },
    {
      "accountId": "2016764541",
      "displayAccountNumber": "XXXXXXXXXXXX0378",
      "transactionDate": "2017-02-26",
      "transactionDescription": "WAL-MART #2649",
      "transactionAmount": 15.82,
      "eligibleForEqualPaymentPlan": "NOT_ELIGIBLE",
      "runningBalanceAmount": 564.84,
      "currencyCode": "USD",
      "transactionStatus": "PENDING",
      "merchantCategory": "5411",
      "merchantDescription": "Grocery Stores, Supermarkets",
      "transactionType": "PURCHASE"
    },
    {
      "accountId": "2016764541",
      "displayAccountNumber": "XXXXXXXXXXXX0378",
      "transactionDate": "2017-02-26",
      "transactionDescription": "UBER AU SEP25 PGPWX HELP.UBER.COM",
      "transactionAmount": 7.66,
      "eligibleForEqualPaymentPlan": "NOT_ELIGIBLE",
      "runningBalanceAmount": 549.02,
      "currencyCode": "USD",
      "transactionStatus": "PENDING",
      "merchantCategory": "5814",
      "merchantDescription": "Fast Food Restaurants",
      "foreignTransactionAmount": 10.11,
      "foreignCurrencyCode": "AUD",
      "transactionType": "PURCHASE"
    },
    {
      "accountId": "2016764541",
      "displayAccountNumber": "XXXXXXXXXXXX0378",
      "transactionDate": "2017-02-12",
      "transactionPostingDate": "2017-02-26",
      "transactionDescription": "SAMSCLUB #6265",
      "transactionAmount": 32.61,
      "eligibleForEqualPaymentPlan": "NOT_ELIGIBLE",
      "runningBalanceAmount": 541.36,
      "currencyCode": "USD",
      "transactionStatus": "UNBILLED",
      "merchantCategory": "5542",
      "merchantDescription": "Fuel Dispenser, Automated",
      "transactionType": "PURCHASE"
    },
    {
      "accountId": "2016764541",
      "displayAccountNumber": "XXXXXXXXXXXX0378",
      "transactionDate": "2017-02-12",
      "transactionPostingDate": "2017-02-26",
      "transactionDescription": "TWC*TIME WARNER CABLE",
      "transactionAmount": 50.18,
      "eligibleForEqualPaymentPlan": "NOT_ELIGIBLE",
      "runningBalanceAmount": 508.75,
      "currencyCode": "USD",
      "transactionStatus": "UNBILLED",
      "merchantCategory": "4899",
      "merchantDescription": "Cable, Satellite, and Other Pay Television and Radio Services",
      "transactionType": "PURCHASE"
    },
    {
      "accountId": "2016764541",
      "displayAccountNumber": "XXXXXXXXXXXX0378",
      "transactionDate": "2017-02-12",
      "transactionPostingDate": "2017-02-26",
      "transactionDescription": "ATS AUSTRALIAN TRAVE SYDNEY",
      "transactionAmount": 80.4,
      "eligibleForEqualPaymentPlan": "NOT_ELIGIBLE",
      "runningBalanceAmount": 458.57,
      "currencyCode": "USD",
      "transactionStatus": "UNBILLED",
      "merchantCategory": "7991",
      "merchantDescription": "Tourist Attractions and Exhibits",
      "foreignTransactionAmount": 106.13,
      "foreignCurrencyCode": "AUD",
      "transactionType": "PURCHASE"
    },
    {
      "accountId": "2016764541",
      "displayAccountNumber": "XXXXXXXXXXXX0378",
      "transactionDate": "2017-02-12",
      "transactionPostingDate": "2017-02-26",
      "transactionDescription": "WAL-MART #2649",
      "transactionAmount": 54.51,
      "eligibleForEqualPaymentPlan": "NOT_ELIGIBLE",
      "runningBalanceAmount": 378.17,
      "currencyCode": "USD",
      "transactionStatus": "UNBILLED",
      "merchantCategory": "5411",
      "merchantDescription": "Grocery Stores, Supermarkets",
      "transactionType": "PURCHASE"
    }
  ]
}

transaction_list = allData['creditCardAccountTransactions']

# print list_of_dict
food_data = []

transaction_dates_set = set()
for t in transaction_list:
  if (t['merchantDescription'].lower().find('food') != -1 or t['merchantDescription'].lower().find('restaurant') !=-1 or t['merchantDescription'].lower().find('eating') != -1):
    if(t['transactionDate'] in transaction_dates_set):
        date_index = next(index for (index, d) in enumerate(food_data) if d["timeStamp"] == t['transactionDate'])
        food_data[date_index]['food'] += t['transactionAmount']
        food_data[date_index]['balance'] = t['runningBalanceAmount']
        print "Found entry for existing date"
        print t['transactionDescription']
    else:
      entry = {}
      entry['timeStamp'] = t['transactionDate']
      transaction_dates_set.add(t['transactionDate'])
      entry['food'] = t['transactionAmount']
      entry['balance'] = t['runningBalanceAmount']
      print "created new entry"
      print t['transactionDescription']
      food_data.append(entry)

customerwise_food_data = {}
customerwise_food_data['customerID'] = transaction_list[0]['accountId']
customerwise_food_data['foodData'] = food_data

print customerwise_food_data

