Crypto Tracker App
Tech Stack: Kotlin, Jetpack Compose, Retrofit, CoinGecko API

Description:
Developed an Android app that tracks real-time information about cryptocurrencies using the CoinGecko API. The app displays a list of cryptocurrencies with details like name, symbol, current price, market cap, and 24-hour price change percentage. Users can also search for specific cryptocurrencies using a search bar for a more customized experience.

Main Screen:
Displays a list of cryptocurrencies with the following details:

Name
ID
Symbol
Current Price
Market Cap Rank
24-Hour Price Change Percentage
Last Updated Timestamp
Search functionality allows users to filter the list by cryptocurrency name or symbol.
Detailed Coin View:
Tapping on any cryptocurrency from the main screen navigates to a detailed page that includes:

ID
Name
Symbol
Small Image
Current Price
Market Cap
24-Hour Price Change Percentage
Ticker Information
A Favorite (fab) button that toggles the crypto's favorite status.
Favorite Coins:
Users can add or remove coins from their favorites list, allowing for quick access to their most followed cryptocurrencies. The app maintains a local list of favorite coins, which can be toggled on the detail page of each coin.

The app integrates the CoinGecko API using Retrofit, ensuring accurate and real-time data for all cryptocurrencies displayed.

## How to Use
- Clone the repository
- Open the project in Android Studio
- Build and run the app