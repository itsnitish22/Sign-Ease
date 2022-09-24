## 🥁Introduction:

- With the utilization of APIs by Hello Sign, we are now able to get the documents signed digitally,
  eliminating the hassle of getting them signed manually. Keeping this feature in mind we had the 
  idea to consume the APIs and utilize this feature for the Indian College Hostels and to put an 
  end to the use of traditional Pass Book method of Applying For Leaves/ Applications. Our Goal
  Through **Sign Ease** is to make the signing of Leaves/ Applications hassle free for the 
  students and to digitalize the traditional method used so far.

## 💡Inspiration:

- College Hostels are one of those authorities that still adheres to the old-fashioned Pass Book system,
  requiring students to maintain a track of the type of Leave/ Application they are requesting for in
  their respective Passbooks. The students are also required to draft the application and get those
  signed manually in those Passbooks by the respective authority member in order to get an approval.
- This traditional process of getting the Leaves/ Applications signed manually has a lot of drawbacks,
  which includes:
  1. Hassle for the student to to Write and Get the applications signed manually.
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
- The backend automatically sends an email with the document to the signers, and user can wait till the
  documents get signed digitally.
  
## 🛠 How we built it

- Hello Sign, Kotlin, XML, Postman, Firebase, APIs.

## ❗Challenges we ran into:

- 
- One of the First challenge was Debugging the Thread Conundrum, due to which our Main thread was
  being terminated before the callback of the network thread.
- Secondly, we weren't able to Pass the auth headers in our API calls.

## ❓ What's next for Feedback Prime

1. Able to add locally hosted media files. And support other media formats.
2. Next, we want to create a cross-platform service to work on a host of Operating Systems.
3. We also plan to add a Graphic visualization and Portable Document Format with the dataset and
   will be automated to send it to the company's e-mail, which can be used for Research and Company
   briefings.
   
##
