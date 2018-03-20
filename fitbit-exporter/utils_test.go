package main

import (
	"testing"
	"time"
)

func TestTimeToFitbitDate(t *testing.T) {
	testData := []struct {
		input  time.Time
		output string
	}{
		{
			time.Date(
				2017, 04, 04, 20, 0, 0, 0, time.UTC),
			"2017-04-04",
		},
		{
			time.Date(
				2020, 12, 21, 20, 0, 0, 0, time.UTC),
			"2020-12-21",
		},
	}

	for _, test := range testData {
		result := timeToFitbitDate(test.input)
		if result != test.output {
			t.Errorf("For value '%v' expected '%s', but got '%s'", test.input, test.output, result)
		}
	}

}
