# Fitbit exporter
> Export data from fitbit into the API


### Build
```bash
$ go get github.com/odinuge/go-graphql
$ go build
$ ./fitbit-exporter -h
```


### Usage
```
Usage of ./fitbit-exporter:
  -apiToken string
        API token
  -apiUrl string
        API url (default "http://localhost:8080/graphql")
  -date string
        Date: format YYYY-MM-DD (default "today")
  -days int
        Days from (including) the given date (default 1)
  -fitbitApiToken string
        Fitbit API token
  -fitbitApiUrl string
        Fitbit API url (default "https://api.fitbit.com/1.2/user/-/")
  -verbose
        Verbose
```

### Example

This example will fetch all data from "2017-02-04" to "2017-02-08". It will then send it into the API.

```bash
$ ./fitbit-exporter -fitbitApiToken="token" -apiToken="graphql-token" -verbose=true -date="2017-02-04" -days=5
```
