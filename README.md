# Calendar-To-do-List

----------------------- Running -----------------------
    - Run the program in Application from src --> gui folder

    - The first scene is StartView
        - Enter Username and Password to Login
        - Enter Username to Start Trial
        - Click "Sign Up" button to create an account
        - Click "Request Temporary Password" button to request a new password by email


    - Sign Up View (When the user click the Sign Up button in the StartView)
        - Enter Email, Username (3-15 char) and Password (6-25 char) to create an account
        - Select User Type from Regular/Anonymous/Admin to become
        - Click "Submit" button to submit what entered above
        - Click "Back" button to go back to the StartView


    - Main Menu View (When the user login or click the "Start Trail" button in the StartView)
        - Click "Creation" button to make creation
        - Click "Profile" button to edit the profile
        - Click "Mail" button to send a message
        - Click "Exit" button to exit from the app
        - Click "Templates" button to edit templates (only for admin user)


    - Creation View (When the user click "Creation" button in the Main Menu View)
        - Click "Add" button to add a creation
            - Select a "Template" include "Todo list"/"Tagged Event list"/"Scheduled list"
        - Click "Owned" button on top to see the user's own creations
            - Select one creation and click "Delete" button to delete the creation that is selected
            - Double Click the selected creation to view and edit the detail of creation
        - Click "Creation Browser" button on top to see all the public creations
            - Select one creation and click "Delete" button to delete the creation that is selected
            - Double Click the selected creation to view and edit the detail of creation
        - Click "Back" button to go back to the Main Menu View


    - New Creation View (When the user select a template in the Creation View)
        - Enter the Name of the creation
        - Click "Add Event" button to add one or more events for this creation (will show up in the scroll panel)
            - Enter the Name of event
            - Enter the Description of event
            - Select whether the event is private or not
            - Enter the Urgency of event (number between 1 to 10)
        - Click "Confirm" button to confirm the creation
        - Click "Back" button to go back to the Creation View


    - Single Creation View (When the user double click the selected creation in Creation View)
        - Click "Rename" button to rename this creation
            - Enter the new name of the creation and click "OK" button
        - Click "Change Privacy" button to change the privacy of this creation
        - Click "Add Event" button to add an event for this creation
            - Enter the new name of the new event and click "OK" button
        - Click "Confirm Changes" button to confirm all the changes in this view for this creation
        - Click "Back" button to go back to the Creation View


    - User Profile View (When the user click "Profile" button in the Main Menu View)
        - Click "Change password" button to change a new password for the current account
        (only if the user is Regular/Anonymous/Admin)
            - Enter a new password in the text field and click "OK" button
        - Click "Ban a user" button to ban a user (only if the current user is Admin)
            - Enter the username of the user to ban the specific user
            - Enter duration in days to ban that user in specific length of days.
        - Click "Sign out" button to sign out from this account
        - Click "Back" button to go back to the Main Menu View


    - Mail View (When the user click "Mail" button in the Main Menu View)
        - Messages will show up in the scroll panel
            - Click the message in the scroll panel to reply the message
        - Click "Compose" button to compose a message
        - Click "Back" button to go back to the Main Menu View


    - Send Message View (When the user click "Compose" button in the Mail View)
        - Enter the receiver's name in the text field next to "To:"
        - Enter the subject in the text field next to "Subject:"
        - Click "Send" button to send the message
        - Click "Back" button to go back to the Mail View
        - Select Creation to attached to this mail
        - Click "Attach" to attach creation that is selected (which will show up in the attachment scroll panel)


    - Single Mail View (When the user click the message in the scroll panel in the Mail View)
        - Detailed message show up in the "Showing Message" text panel
        - Click "Reply" button to reply on this message
        - Click the attachments under the attachment scroll panel to see the creation
        - Click "Back" button to go back to the Mail View


    - Template View (When the admin user click the "Templates" button in Main Menu View)
        - Select the template in the table and click the "Edit Contents" button to edit the contents of the template
        - Select the template in the table and click the "Rename" button to rename the template
        - Click "Back" button to go back to the Main Menu View


    - Edit Template View (When the admin user click the "Edit Contents" button in Template View)
        - In the scroll panel
            - Enter information that user want to edit
        - Click "Confirm" button to confirm the edited version
        - Click "Back" button to go back to the Template View


----------------------- Savestate info -----------------------

    - empty
        - no users
        - no creations or events
        - have 3 default templates loaded (see txt files in templates folder)
                - no mail

    - multi_user
        - 3 users: bobjoe, john doe, adminguy (see user_list.txt for more information)
        - 5 creations in total:
            - bobjoe: bob todo 1, bob schedule, bob private todo
            - john doe: john todo, john schedule
            - each is filled with 1-4 events
