package main

import (
	"time"
	"fmt"
)

type ActivitySummaryData struct {
	Goals struct {
		ActiveMinutes int `json:"activeMinutes"`
		CaloriesOut   int `json:"caloriesOut"`
		Distance      int `json:"distance"`
		Floors        int `json:"floors"`
		Steps         int `json:"steps"`
	} `json:"goals"`
	Summary struct {
		ActiveScore      int `json:"activeScore"`
		ActivityCalories int `json:"activityCalories"`
		CaloriesBMR      int `json:"caloriesBMR"`
		CaloriesOut      int `json:"caloriesOut"`
		Distances []struct {
			Activity string  `json:"activity"`
			Distance float64 `json:"distance"`
		} `json:"distances"`
		Elevation           float64 `json:"elevation"`
		FairlyActiveMinutes int     `json:"fairlyActiveMinutes"`
		Floors              int     `json:"floors"`
		HeartRateZones []struct {
			CaloriesOut float64 `json:"caloriesOut"`
			Max         int     `json:"max"`
			Min         int     `json:"min"`
			Minutes     int     `json:"minutes"`
			Name        string  `json:"name"`
		} `json:"heartRateZones"`
		LightlyActiveMinutes int `json:"lightlyActiveMinutes"`
		MarginalCalories     int `json:"marginalCalories"`
		RestingHeartRate     int `json:"restingHeartRate"`
		SedentaryMinutes     int `json:"sedentaryMinutes"`
		Steps                int `json:"steps"`
		VeryActiveMinutes    int `json:"veryActiveMinutes"`
	} `json:"summary"`
}

func fetchActivityData(givenDate time.Time, data *ActivitySummaryData) error {
	path := fmt.Sprintf("activities/date/%s.json", timeToFitbitDate(givenDate))
	return fetchData(givenDate, data, path)
}
