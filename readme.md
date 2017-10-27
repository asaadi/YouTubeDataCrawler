## YouTube Data Crawler - Java/Android

This is a pure Java library helps crawl data from YouTube without using YouTube Data API. The library will warranty
compatible Java 7+ **and** Android SDK 16+.

## What you can crawl?
This library try to crawl information of `Playlist`, `Video`, `Channel` (, and user private data in future).
Feature checklist:
  - YouTube home: `/`
    + [ ] Recommended channel section: `ChannelBasic` + recommended `List<VideoBasic>` of this channel
    + [ ] Recommended playlist: `PlaylistBasic` + recommended `List<VideoBasic>` of this playlist
    + [ ] Youtube Mixes: `List<PlaylistBasic>` YouTube mixed playlist list
  - Feed trending: `/feed/trending`
    + [ ] `List<Video>` display on trending page
  - Watching video: `/watch?v=video_id`
    + [ ] `Video`: Full information of video
    + [ ] `List<VideoBasic>`: List related videos
  - Watching playlist: `/watch?v=video_id&list=playlist_id`
    + [ ] `Video`: full information of current playing video
    + [ ] `List<BasicVideo>`: List related videos of current playing video
    + [ ] `List<BasicVideo>`: List video of playlist (maximum 200 video)
  - Playlist: `/playlist?list=playlist_id`
    + [ ] `Playlist`: information of the playlist
    + [ ] `List<BasicVideo>`: List all videos of the playlist
  - Channel Home: `/user/user_id[/featured]` or `/channel/channel_id[/featured]`

TODO: Update read me

## Dependencies

- RxJava 1 (will upgrade to RxJava 2 soon)
- OkHttpClient