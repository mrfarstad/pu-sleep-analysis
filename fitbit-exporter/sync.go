package main

import (
	"time"
	"fmt"
)

func syncSleepData(givenDate time.Time) {
	var sleepData SleepData
	err := fetchSleepData(givenDate, &sleepData)
	if err != nil {
		fmt.Errorf("error downloading sleep data %e", err)
	}
	levelData := aggregateSleepLevelData(sleepData)

	if verbose {
		fmt.Printf("For date: %s\n", timeToFitbitDate(givenDate))
		for _, sleepLevel := range levelData {
			fmt.Printf(" - Level %s got %d minutes\n", sleepLevel.Level, sleepLevel.Seconds/60)
		}
	}

	// TODO sync to api
}

func syncActivityData(givenDate time.Time) {
	var activitySummaryData ActivitySummaryData
	err := fetchActivityData(givenDate, &activitySummaryData)
	if err != nil {
		fmt.Errorf("error downloading activityData data %e", err)
	}

	if verbose {
		fmt.Printf("For date: %s\n", timeToFitbitDate(givenDate))
		fmt.Printf(" - Got %d steps\n", activitySummaryData.Summary.Steps)
	}

}
