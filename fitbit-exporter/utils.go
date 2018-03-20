package main

import (
	"context"
	"encoding/json"
	"fmt"
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
	req.Header.Set("Authorization", "Bearer "+apiToken)

	ctx := context.Background()

	resp := struct{}{}

	if err := client.Run(ctx, req, resp); err != nil {
		fmt.Println("Error calling API")
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
		panic(err)
	}

	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Println("Error fetching FitbitAPI")
		panic(err)
	}
	if resp.StatusCode != 200 {
		fmt.Printf("Response: %s\n", body)
		panic(fmt.Sprintf("Error fetching API. statusCode: %d\n", resp.StatusCode))

	}
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
