## 🥁Introduction:

- With the utilization of APIs by Hello Sign, we are now able to get the documents signed digitally,
  eliminating the hassle of getting them signed manually. Keeping this feature in mind we had the 
  idea to consume the APIs and utilize this feature for the **Indian College Hostels** and to put an 
  end to the use of traditional Pass Book method of Applying For Leaves or submitting Applications 
  and keeping a track of them. Our Goal through **Sign Ease** is to "Ease" the signing of Leaves and
  Applications for the students and for the Authority Members, and ultimately to digitalize the 
  traditional method used so far.

## 💡Inspiration:

- College Hostels are one of those authorities that still adheres to the old-fashioned Pass Book system,
  requiring students to maintain a track of the type of Leaves and Applications they are requesting for in
  their respective Passbooks. The students are also required to draft the application and get those
  signed manually in the Passbooks by the respective authority member in order to get an approval.
- This traditional process of getting the Leaves/ Applications signed manually has a lot of drawbacks,
  which includes:
  1. Hassle for the student to Write and Get the Applications signed manually.
  2. In case of Application For Night Pass, the student has to get the application approved both by the 
     Hostel's Warden and Parent/ Guardian, which increases the time for getting the approval.
  3. If a large number of Applications are received to the Hostel's Warden, it becomes difficult for
     them to Manage, Review and Sign the Applications.
- Keeping all these issues in mind, we came up with the concept to Digitalize this approach by building
  Sign Ease, an Android application that has integrated APIs by **Hello Sign** and assists the students 
  in getting their Leaves and Applications signed Digitally by the respective Authority Member. Also it 
  helps the Wardens/ Authority Members to keep a track all the applications that they've signed.

## 💬 What it does:

- User chooses the Application or a Leave (Duty Leave, Day Pass or Night Pass) that they want to get signed.
- After the input of Dates (Leave Start, Leave End) and a Reason for the Leave, user can review the application
  and can click confirm. 
- The backend automatically sends an email with the document to the signers, who are by default Mapped (along with their emails)
  with the type of Role and the application selected. The user can wait till the documents get signed digitally and 
  can download the signed documents or delete their pending requests (if they wish to).
  
  ![image](https://user-images.githubusercontent.com/73310532/192137580-67f39f14-57bb-4f38-9205-bb32b057234a.png)
  
## 🛠 How we built it

- Hello Sign, Kotlin (MVVM, NavGraphs, Coroutines, Retrofit), XML, Postman, Firebase, APIs, Figma.

## ❗Challenges we ran into:

- Integrating and Loading PDFs in a WebView within a Bottom Sheet Dialog.
- Handling crashes due to a lag in the Processing Time of API calls.
- Managing the Fragment Stack and its Lifecycle because of multiple Navigations.

## ❓ What's next for Sign Ease

1. Able to embedd logs of Leaves and the Applications on the identity cards of students.
2. Adding more Templates and Signers across the spectrum of a College.
3. Build a cross-platform service that can work on multiple Opeating Systems.
4. Build another Portal for the Authorities to sign the documents then and there instead of
   opening the e-mails again and again. All by keeping **Hello Sign** as the backbone.
   
##
