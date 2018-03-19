package main

import (
	"context"
	"encoding/json"
	"github.com/odinuge/go-graphql"
	"io/ioutil"
	"net/http"
	"time"
)

func sendToGraphqlApi(query string, vars map[string]string) {
	client := graphql.NewClient(apiUrl)
	req := graphql.NewRequest(query)

	for key, value := range vars {
		req.Var(key, value)
	}
	req.Header.Set("Authentication", "Bearer "+apiToken)

	ctx := context.Background()

	if err := client.Run(ctx, req, nil); err != nil {
		panic(err)
	}
}

func fetchAPI(endpoint string) ([]byte, error) {
	url := fitbitApiUrl + endpoint
	req, err := http.NewRequest("GET", url, nil)
	bearer := "Bearer " + fitbitApiToken
	req.Header.Add("Authorization", bearer)

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return nil, err
	}

	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	return body, err

}

func fetchData(givenDate time.Time, data interface{}, path string) error {
	output, err := fetchAPI(path)
	if err != nil {
		panic(err)
	}
	return json.Unmarshal(output, &data)
}

func timeToFitbitDate(date time.Time) string {
	return date.Format("2006-01-02")
}
