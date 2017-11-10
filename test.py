from tweepy.streaming import StreamListener
import tweepy

consumer_key = 'S9MDz2VQC1st3e1OaM27MoqOR'
consumer_secret = '5q1miYIVeWuNUyB7He64hP35dkwBfvX2F6fq5e2jPt1EzeFB6R'
access_token = '1556178751-r3WvvDh2praSg5RqCvJ8GbXM3d8E1Xx7nMPMS9E'
access_token_secret = 'TJHTGGze5GmStYAnRjYGDxGYHcZ1fSi8oXN1yjwXAVoqX'
auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)

api = tweepy.API(auth)

#public_tweets = api.home_timeline()
#for tweet in public_tweets:
#    print tweet.text
class StdOutListener(StreamListener):

    def on_data(self, data):
        print data
        return True

    def on_error(self, status):
        print status


if __name__ == '__main__':

    #This handles Twitter authetification and the connection to Twitter Streaming API
    consumer_key = 'S9MDz2VQC1st3e1OaM27MoqOR'
    consumer_secret = '5q1miYIVeWuNUyB7He64hP35dkwBfvX2F6fq5e2jPt1EzeFB6R'
    access_token = '1556178751-r3WvvDh2praSg5RqCvJ8GbXM3d8E1Xx7nMPMS9E'
    access_token_secret = 'TJHTGGze5GmStYAnRjYGDxGYHcZ1fSi8oXN1yjwXAVoqX'
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    api = tweepy.API(auth)
    
    l = StdOutListener()
    stream = tweepy.Stream(auth, l)

    #This line filter Twitter Streams to capture data by the keywords: 'python', 'javascript', 'ruby'
    stream.filter(track=['python', 'javascript', 'ruby'])
