# Smart Departure Transit Notification
John Brown, Eric Hart, Steven Nardo, Jake Wells

## Trello Board
https://trello.com/b/dd7aJfuv/sdtm

## Vision Statement
For public transit users who miss their trains/busses, Smart Transit Departure Notification (clever name pending) is an app that presents departure times from nearby public transit stations to the user in the form of a persistent notification on their phone. Unlike the Septa app, our product makes finding the next departure times at any station as easy as looking at your lockscreen.

## Project Abstract

I would like to develop a public transport notification app for Android. Currently, I can use the Septa app to select a desired route, I can favorite the route and use the app to see the next departing trains along that route. But, I need to constantly reopen the app to do this. If I want to take a route outside of this, I need to look up that specific route again.

With this app, the user should be able to select a favorite route and be able to view the next departing trains/busses as a persistent notification on their phone’s lock screen. The user should see the next departing train, and be able to expand the notification to see the next three departing trains along that route.

Additionally, the user should be able to give the app location permission, and the app should be able to locate nearby transit stops in order to present the next departure times at those stations as a separate persistent notification. The user should be able to set preference for subway/bus routes for this feature, so that they don’t get flooded with many nearby routes that they do not know or travel on. But, if the user is at a bar in an area of the city that they do not frequent, they should be able to open their lock screen to see the next departure times from the nearest subway station going in either direction, even though that route is not on their list of followed/favorite routes.

Finally, the user should have the option to mark that they have gotten onto a transit route, and the app should be able to set an alarm that will go off just before they arrive. The user should be able to quickly pick a destination through the app, as well as to mark a destination as a favorite. They should ideally be able to do all of this from a notification on their lockscreen. The app should set the alarm based on arrival time by default, or by location for a more accurate result. Wit this, the app should also be able to predict some delays naturally.

### Resources
Java, Android Studio, Google Maps API and/or SEPTA API, Neura API


## Personas

#### Persona 1: Steven Nardo
Miranda, aged 24, is a manager at a café in center city Philadelphia. She often times both opens and closes the store when working, usually during weekends as well as weekdays. She is from a town in south New Jersey; her father is a plumber and active in his local union and her mother is a nurse anesthetist at their local hometown hospital. Miranda attended Drexel University for Marketing and has lived in Philadelphia, the neighborhood of Queens Village, since starting her bachelor’s degree, i.e. for around 6 years.

Miranda’s frequent use of SEPTA transit services such as the subway and the public bus system, as well as the PATCO lines makes her adept in traversing public transit and not a stranger to using multiple transit methods at once. This being said, working such strenuous hours and at various times of the day often result in Miranda falling asleep or getting distracted during her commute to and from work. A feature such as the departure notification would greatly ease her multi-step commute as well as ease her anxiety about missing any stops.

#### Persona 2: Jake Wells
Billy, aged 27, is an office worker living in University City. Billy is from outside of Boston, and he moved to Philadelphia for work. He has a degree in Printed Document File Management from Boston College. His father is a mime, and his mother won a bronze medal in skiing at the '94 Olympic Winter Games in Lillehammer, Norway.

Billy uses a computer for his job as well as for internet browsing and streaming. On the weekends, Billy likes to go out with his friends. This brings him to many different parts of the city. Billy prefers to use the subway because of its simplicity and because of a lack of familiarity with the bus system. Billy finds himself in odd parts of the city at odd times, so being able to quickly know the nearest subway station to him, the next departure times from that station, and the last trip of the day would be invaluable to Billy to ease his commute home.

#### Persona 3: John Brown
Joshee, age 22, is a student at Temple University. He grew up in South Eastern, PA, on a small farmhouse, with his father, who is a large animal veterinarian. He was an active member of his high school's marching band, and continued his involvement with marching band into college. While he initially came to Temple to study biology he eventually changed his major to political science.  

Joshee lives on campus, and recently got hired as an intern at a law firm in center city. Because of his somewhat rural upbringing, he is having quite a bit of trouble getting accustomed to the array of trains, trolleys, and buses he needs to use to get to work and back.  Another side affect of his rural upbringing is an unfamiliarity with technology. He would like something that would simply tell him where to be to get on a route, and then notify him when to get off/transfer. Preferably, as a notification he can view on his home screen so he doesn't have to navigate menus, dropdowns, and settings.  

#### Persona 4: Eric Hart

Chris, age 17, is a high school student living in Downingtown, PA where he was born and raised. His father works in IT for an insurance company and his mother is a paralegal. Chris has no aspirations as to what he will do with his life outside of high school but is somewhat invested in his endeavours as a soundcloud rapper under the name "Yung Void". He currently works as a part time cashier at Wawa.

Chris enjoys visiting Philadelphia on weekends with his friends to see live music performances and attend college parties. Neither him or his friends own a car so they take the regional rail and local trains to get to and around the city. Chris and his friends are still new to subway system and often times find themselves confused and stressed when trying to make it to their trains on time. An application that can familiarize Chris with the train system and send push notifications of departure times would ease the anxiety of navigating SEPTA and create a more positive experience for Chris and his friends when they want to go to the city.
