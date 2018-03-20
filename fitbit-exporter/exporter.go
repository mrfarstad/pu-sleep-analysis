package main

import (
	"flag"
	"fmt"
	"time"
)

var fitbitApiToken string
var userId string

var apiToken string

var apiUrl string
var fitbitApiUrl string

var verbose bool

var dateString string

var days int

func init() {
	flag.StringVar(&fitbitApiToken, "fitbitApiToken", "", "Fitbit API token")
	flag.StringVar(&fitbitApiUrl, "fitbitApiUrl", "https://api.fitbit.com/1.2/user/-/", "Fitbit API url")
	flag.StringVar(&apiUrl, "apiUrl", "http://localhost:8080/graphql", "API url")
	flag.StringVar(&apiToken, "apiToken", "", "API token")
	flag.BoolVar(&verbose, "verbose", false, "Verbose")

	flag.StringVar(&dateString, "date", "today", "Date: format YYYY-MM-DD")
	flag.IntVar(&days, "days", 1, "Days from (including) the given date")
}

func main() {
	flag.Parse()

	var err error
	date := time.Now()

	if dateString != "today" {
		date, err = time.Parse("2006-01-02", dateString)
	}

	if err != nil {
		fmt.Printf("Unable to parse date: %s\n", dateString)
		panic(err)
	}

	for i := 0; i < days; i++ {
		syncAll(date)
		date = date.AddDate(0, 0, 1)
	}

}
