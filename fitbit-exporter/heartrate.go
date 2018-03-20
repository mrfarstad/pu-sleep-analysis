package main

import (
	"fmt"
	"time"
)

type HeartRateData struct {
	ActivitiesHeart []struct {
		DateTime string `json:"dateTime"`
		Value    struct {
			CustomHeartRateZones []interface{} `json:"customHeartRateZones"`
			HeartRateZones       []struct {
				CaloriesOut float64 `json:"caloriesOut"`
				Max         int     `json:"max"`
				Min         int     `json:"min"`
				Minutes     int     `json:"minutes"`
				Name        string  `json:"name"`
			} `json:"heartRateZones"`
			RestingHeartRate int `json:"restingHeartRate"`
		} `json:"value"`
	} `json:"activities-heart"`
	ActivitiesHeartIntraday struct {
		Dataset []struct {
			Time  string `json:"time"`
			Value int    `json:"value"`
		} `json:"dataset"`
		DatasetInterval int    `json:"datasetInterval"`
		DatasetType     string `json:"datasetType"`
	} `json:"activities-heart-intraday"`
}

func fetchHeartRateData(givenDate time.Time, data *HeartRateData) error {
	path := fmt.Sprintf("activities/heart/date/%s/1d.json", timeToFitbitDate(givenDate))
	return fetchData(givenDate, data, path)
}
