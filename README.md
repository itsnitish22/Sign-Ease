## 🥁Introduction:

- With the utilization of APIs by Hello Sign, we are now able to get the documents signed digitally,
  eliminating the hassle of getting them signed manually. Keeping this feature in mind we had the 
  idea to consume the APIs and utilize this feature for the Indian College Hostels and to put an 
  end to the use of traditional Pass Book method of Applying For Leaves/ Applications and keeping a track of them. Our Goal
  Through **Sign Ease** is to "Ease" the signing of Leaves/ Applications for the 
  students and to digitalize the traditional method used so far.

## 💡Inspiration:

- College Hostels are one of those authorities that still adheres to the old-fashioned Pass Book system,
  requiring students to maintain a track of the type of Leave/ Application they are requesting for in
  their respective Passbooks. The students are also required to draft the application and get those
  signed manually in those Passbooks by the respective authority member in order to get an approval.
- This traditional process of getting the Leaves/ Applications signed manually has a lot of drawbacks,
  which includes:
  1. Hassle for the student to Write and Get the Applications signed manually.
  2. In case of Application For Night Pass, the student has to get the application approved both by the 
     Hostel's Warden and Parent/ Guardian, which increases the time for getting the approval.
  3. If a large number of Applications are received to the Hostel's Warden, it becomes difficult for
     them to Manage, Review and Sign the Applications.
- Keeping all these issues in mind, we came up with the concept to Digitalize this approach by building
  Sign Ease, an Android application that has integrated APIs by **Hello Sign** and assists the students 
  in getting their Leaves/ Applications signed Digitally by the respective authority member. Also it 
  helps the Wardens/ Authority Members to keep a track of whose applications they've signed.

## 💬 What it does:

- User chooses the Application/ Leave (Duty Leave, Day Pass or Night Pass) they want to get signed.
- After the input of Dates (Leave Start, Leave End) and Reason, user can review the application and click
  confirm. 
- The backend automatically sends an email with the document to the signers, who are by default mapped with the type 
  of role and the application selected, and user can wait till the documents get signed digitally or delete their pending requests.
  
  ![image](https://user-images.githubusercontent.com/73310532/192137580-67f39f14-57bb-4f38-9205-bb32b057234a.png)
  
## 🛠 How we built it

- Hello Sign, Kotlin (MVVM, NavGraphs, Coroutines, Retrofit), XML, Postman, Firebase, APIs, Figma.

## ❗Challenges we ran into:

- Integrating and Loading PDFs in WebView within a Bottom Sheet Dialog.
- Handling crashes due to lag in the processing time of API calls.
- Managing the Fragment Stack and its Lifecycle because of multiple Navigations.

## ❓ What's next for Sign Ease

1. Able to embedd logs of Leaves/ Application on the identity cards of students.
2. Adding more Templates and Signers across the spectrum of a College.
3. Build a cross-platform service that can work on a host of Opeating Systems.
4. Build another log-in portal for authorities to sign the documents there 
  itself rather than opening mail again and again by keeping Hello-Sign as the backbone.
   
##
