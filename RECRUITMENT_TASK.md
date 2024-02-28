## Recruitment Task
Introducing the 'Always Right Temp Inc' 🌡🌞, the masters of
temperature sensors! They've bestowed upon you the honor of
creating an anomaly detector, sprinkled with a dash of visual magic
through additional APIs. They trust YOU completely to handle this
challenge! 🤝
Now, let's dive into the technical nitty-gritty. Since 'Always Right Temp
Inc' has already invested in Kafka, we'll be using that fancy
technology. Unfortunately, they couldn't ship physical thermometers
(bummer!). But fear not! You shall conquer this obstacle by building a
Temperature Measurement Generator that can handle a whopping
20k measurements per second.
Behold, the majestic system diagram they've prepared for us. Feast
your eyes upon its grandeur!
But wait, how do we calculate anomalies, you ask? 🤔 Fear not, my
friend, for our team has graciously provided two algorithms for your
consideration. Let's choose the one that suits our needs based on the
properties file.

# Anomaly Detection Algorithm ONE:
In this algorithm, we label a temperature measurement as an anomaly
if, within any 10 consecutive temperature measurements, there exists
one measurement that surpasses the average of the remaining 9
measurements by a whopping 5 degrees Celsius. Let's illustrate with
an example:
20.1, 21.2, 20.3, 19.1, 20.1, 19.2, 20.1, 18.1, 19.4, 20.1, 27.1, 23.1
See that? We've got an anomaly! The temperature measurement of
27.1 boldly stands out from the rest.

# Now, let's move on to Anomaly Detection Algorithm TWO:
In this exciting algorithm, an anomaly is defined as any temperature
measurement that exceeds the mean of all measurements by 5
degrees Celsius within a 10-second window based on the
measurement timestamp. Prepare for another example:
19.1 1684945005
19.2 1684945006
19.5 1684945007
19.7 1684945008
19.3 1684945009
24.1 1684945010
18.2 1684945011
19.1 1684945012
19.2 1684945013
23.4 1684945015
Hold your horses! We've got not one, but two anomalies! The daring
measurements of 24.1 and 23.4 surpass the mean with their fiery
passion.
But that's not all! Once you've mastered the art of Temperature
Anomaly Analyzer, make sure to store that precious data and make
it available for visualization through the implementation of all
endpoints.
And lo and behold, don't forget to grace us with the presence of unit
and integration tests! These noble warriors shall bravely ensure the
quality and functionality of our masterpiece 🖼
Your GitHub skills shall shine as you upload the code into a
repository. Share the link with us, and may the fun and excitement be
with you as we cross our fingers 🤞 for a legendary outcome! Let's
make 'Always Right Temp Inc' proud! 🙂