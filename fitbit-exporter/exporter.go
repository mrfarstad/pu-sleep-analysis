package main

import (
	"flag"
	"time"
)

var fitbitApiToken string
var userId string

var apiToken string

var apiUrl string
var fitbitApiUrl string

var verbose bool

func init() {
	flag.StringVar(&fitbitApiToken, "fitbitApiToken", "", "Fitbit API token")
	flag.StringVar(&fitbitApiUrl, "fitbitApiUrl", "https://api.fitbit.com/1.2/user/-/", "Fitbit API url")
	flag.StringVar(&apiUrl, "apiUrl", "http://localhost:8080/graphql", "API url")
	flag.StringVar(&apiToken, "apiToken", "", "API token")
	flag.BoolVar(&verbose, "verbose", false, "Verbose")
}

func main() {
	flag.Parse()

	for i := 0; i < 10; i++ {
		date := time.Date(
			2017, 04, i, 20, 0, 0, 0, time.UTC)
		syncSleepData(date)
		syncActivityData(date)
	}

}

