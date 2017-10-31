## YouTube Data Crawler - Java/Android

This is Java library that helps to get YouTube Data without using YouTube Data API, the result is
whatever about Video, Channel, Playlist display on various YouTube endpoint.

It also can get private data such as private mixes playlist, account's playlist,... To accomplish
this, just set cookie via OkHttp's [CookieJar](https://square.github.io/okhttp/3.x/okhttp/okhttp3/CookieJar.html) implementation.

## What it can crawl?

| Status | Page | Endpoint |
|:------:|:----:|:--------|
| [x] | Home feed | `/` |
| [x] | Trending feed | `/feed/trending` |
| [x] | Search result | `/results?search_query=xxx` |
| [x] | Channel home | `/channel/xx_channel_id_xx/featured` |
| [x] | Channel's videos | `/channel/xx_channel_id_xx/videos` |
| [x] | Channel's playlists | `/channel/xx_channel_id_xx/playlists` |
| [x] | Channel's channels | `/channel/xx_channel_id_xx/channels` |
| [ ] | Playlist | `/playlist?list=xx_playlist_id_xx` |
| [ ] | Watching video | `/watch?v=xx_video_id_xx` |
| [ ] | Watching playlist | `/watch?v=xx_video_id_xx&list=xx_playlist_id_xx` |

## Dependencies

 - [OkHttp](https://github.com/square/okhttp)
 - [Gson](https://github.com/google/gson)
 - [RxJava1](https://github.com/ReactiveX/RxJava/tree/1.x)

