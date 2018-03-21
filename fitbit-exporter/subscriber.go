package main

type SubscriptionPaylod []struct {
	CollectionType string `json:"collectionType"`
	Date           string `json:"date"`
	OwnerID        string `json:"ownerId"`
	OwnerType      string `json:"ownerType"`
	SubscriptionID string `json:"subscriptionId"`
}
