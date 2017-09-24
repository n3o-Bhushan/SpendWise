import pandas as pd
import numpy as np
import json

json_string = {
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
df = pd.DataFrame.from_records(map(json.loads, json_lst))

