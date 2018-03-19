package main

import (
	"time"
	"fmt"
)

type SleepData struct {
	Sleep []struct {
		DateOfSleep string `json:"dateOfSleep"`
		Duration    int    `json:"duration"`
		Efficiency  int    `json:"efficiency"`
		EndTime     string `json:"endTime"`
		InfoCode    int    `json:"infoCode"`
		IsMainSleep bool   `json:"isMainSleep"`
		Levels struct {
			Data []struct {
				DateTime string `json:"dateTime"`
				Level    string `json:"level"`
				Seconds  int    `json:"seconds"`
			} `json:"data"`
			Summary struct {
				Asleep struct {
					Count   int `json:"count"`
					Minutes int `json:"minutes"`
				} `json:"asleep"`
				Awake struct {
					Count   int `json:"count"`
					Minutes int `json:"minutes"`
				} `json:"awake"`
				Restless struct {
					Count   int `json:"count"`
					Minutes int `json:"minutes"`
				} `json:"restless"`
			} `json:"summary"`
		} `json:"levels"`
		LogID               int64  `json:"logId"`
		MinutesAfterWakeup  int    `json:"minutesAfterWakeup"`
		MinutesAsleep       int    `json:"minutesAsleep"`
		MinutesAwake        int    `json:"minutesAwake"`
		MinutesToFallAsleep int    `json:"minutesToFallAsleep"`
		StartTime           string `json:"startTime"`
		TimeInBed           int    `json:"timeInBed"`
		Type                string `json:"type"`
	} `json:"sleep"`
	Summary struct {
		TotalMinutesAsleep int `json:"totalMinutesAsleep"`
		TotalSleepRecords  int `json:"totalSleepRecords"`
		TotalTimeInBed     int `json:"totalTimeInBed"`
	} `json:"summary"`
}

type SleepLevel struct {
	Level   string
	Seconds int
}

func fetchSleepData(givenDate time.Time, data *SleepData) error {
	path := fmt.Sprintf("sleep/date/%s.json", timeToFitbitDate(givenDate))
	return fetchData(givenDate, data, path)
}

func aggregateSleepLevelData(sleepData SleepData) []SleepLevel {
	m := make(map[string]int)
	for _, sleep := range sleepData.Sleep {
		for _, level := range sleep.Levels.Data {
			m[level.Level] = level.Seconds + m[level.Level]
		}
	}

	v := make([]SleepLevel, 0, len(m))

	for level, sum := range m {
		v = append(v, SleepLevel{
			Level: level,
			Seconds:sum,
		})
	}
	return v
}
