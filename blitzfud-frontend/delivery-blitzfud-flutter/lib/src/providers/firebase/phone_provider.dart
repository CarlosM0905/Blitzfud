// IMPORTS
import 'package:firebase_database/firebase_database.dart';

class PhoneNumberProvider {
  DatabaseReference mDatabase;

  PhoneNumberProvider() {
    mDatabase = FirebaseDatabase.instance.reference().child("Delivery");
  }

  Future<void> create(String phoneNumber) async {
    await mDatabase.child(phoneNumber).set({'role': 'USER'});
  }

  Future<bool> exists(String phoneNumber) async {
    DataSnapshot data = await mDatabase.child(phoneNumber).once();

    return data.value != null;
  }
}
