package main

import (
	"fmt"
	"strconv"
	"time"
)

func syncSleepData(givenDate time.Time) {
	var sleepData SleepData
	err := fetchSleepData(givenDate, &sleepData)
	if err != nil {
		fmt.Printf("error downloading sleep data %e\n", err)
	}
	levelData := aggregateSleepLevelData(sleepData)

	query := `
mutation ($date: String!, $duration: Int!, $efficiency: Int!, $userId: ID!) {
	createSleepData(date: $date, duration: $duration, efficiency: $efficiency, userId: $userId) {
		id
	}
}`

	variables := make(map[string]string)
	variables["efficiency"] = "0"
	variables["duration"] = "0"
	variables["date"] = timeToFitbitDate(givenDate)
	variables["userId"] = apiToken

	for _, sleep := range sleepData.Sleep {
		if sleep.IsMainSleep {
			variables["efficiency"] = strconv.Itoa(sleep.Efficiency)
			variables["duration"] = strconv.Itoa(sleep.Duration / 60000)
		}

	}

	if verbose {
		for level, secounds := range levelData {
			fmt.Printf(" - Level %s got %d minutes\n", level, secounds/60)
		}
		fmt.Printf(" - Sleep efficiency %s\n", variables["efficiency"])

	}

	sendToGraphqlApi(query, variables)
}

func syncActivityData(givenDate time.Time) {
	var activitySummaryData ActivitySummaryData
	err := fetchActivityData(givenDate, &activitySummaryData)
	if err != nil {
		fmt.Printf("error downloading activityData data %e\n", err)
		return
	}

	if verbose {
		fmt.Printf(" - Steps: %d \n", activitySummaryData.Summary.Steps)
	}
	const query = `
mutation ($date: String!, $steps: Int!, $userId: ID!){
	createStepsData(date: $date, steps: $steps, userId: $userId) {
		id
	}
}`
	variables := make(map[string]string)
	variables["steps"] = strconv.Itoa(activitySummaryData.Summary.Steps)
	variables["date"] = timeToFitbitDate(givenDate)
	variables["userId"] = apiToken
	sendToGraphqlApi(query, variables)
}

func syncHRData(givenDate time.Time) {

	var data HeartRateData
	err := fetchHeartRateData(givenDate, &data)
	if err != nil {
		fmt.Printf("error downloading activityData data %e\n", err)
		return
	}

	// We only fetch one date, and therefore we just use the first day
	if len(data.ActivitiesHeart) == 0 {
		fmt.Println(" - Unable to find restingHr")

		return
	}

	restingHr := data.ActivitiesHeart[0].Value.RestingHeartRate

	if verbose {
		fmt.Printf(" - Resting HR %d\n", restingHr)
	}

	variables := make(map[string]string)
	variables["date"] = timeToFitbitDate(givenDate)
	variables["userId"] = apiToken
	variables["restHr"] = strconv.Itoa(restingHr)

	const query = `
mutation ($date: String!, $restHr: Int!, $userId: ID!){
	createPulseData(date: $date, restHr: $restHr, userId: $userId) {
		id
	}
}`
	// Not yet implemented
	// sendToGraphqlApi(query, variables)

}

func syncAll(date time.Time) {
	if verbose {
		fmt.Printf("Date: %s\n", timeToFitbitDate(date))
	}
	syncSleepData(date)
	syncActivityData(date)
	syncHRData(date)
	if verbose {
		fmt.Println()
	}
}
