import firebase from "firebase/app";
import "firebase/database";

var firebaseConfig = {
  apiKey: "AIzaSyDIQOL1B0_ferRyc0jpyv5j-22Ieg3fSjg",
  authDomain: "dziczyzna-b91b0.firebaseapp.com",
  databaseURL: "https://dziczyzna-b91b0.firebaseio.com",
  projectId: "dziczyzna-b91b0",
  storageBucket: "dziczyzna-b91b0.appspot.com",
  messagingSenderId: "788347894314",
  appId: "1:788347894314:web:aeeea6ebfd0339e0f8e153",
  measurementId: "G-WRFHYPM34H",
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

const database = firebase.database();

export { database };
