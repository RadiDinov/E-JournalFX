package DataCenter;

public class StoredData {
    //TODO: Future Addon

    //                //If wanted to add teacher
//                else if (positionField.getText().equals("Teacher")) {
//
//                    //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
//                    jdbcAddNewTeacher = new JDBC("select * from teachers", "INSERT INTO teachers (firstName, lastName, email, classTeacherOfASection) VALUES (?, ?, ?, ?)");
//                    //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
//
//                    isThereATableThatEqualsSection = false;
//
//                    while (jdbcGetTables.getResultSet().next()) {
//                        System.out.println(classNumberFieldModifiedForSQL);
//                        System.out.println(jdbcGetTables.getResultSet().getString(1));
//
//                        //If there is already a table with this section
//                        if (classNumberFieldModifiedForSQL.equals(jdbcGetTables.getResultSet().getString(1))) {
//                            isThereATableThatEqualsSection = true;
//                            break;
//                        }
//                        //If there is already a table with this section
//
//                    }
//
//
//                    //TODO: optimize and remove!
//                    jdbcGetTables.resultSet.beforeFirst(); //TODO: test if can be removed
//                    //TODO: optimize and remove!
//
//                    //If section is inserted
//                    if (!(classNumberField.getText().equals("-")) || classNumberField.getText().length() != 3) {
//
//                        //If inserted section exist
//                        if (isThereATableThatEqualsSection) {
//
//                            //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
//                            jdbcAddNewStudent = new JDBC("select * from " + classNumberFieldModifiedForSQL, "INSERT INTO " + classNumberFieldModifiedForSQL + " (firstName, lastName, classNumber, position) VALUES (?, ?, ?, ?)");
//                            //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
//
//                            jdbcAddNewStudent.resultSet.absolute(1);
//
//                            //If in the section's SQL table there isn't a class teacher
//                            if (!jdbcAddNewStudent.resultSet.next()) {
//
//                                //Adding teacher in registrations table
//                                jdbcAddNewRegistration.writeData.setString(1, classNumberField.getText());
//                                jdbcAddNewRegistration.writeData.setString(2, firstNameField.getText());
//                                jdbcAddNewRegistration.writeData.setString(3, lastNameField.getText());
//                                jdbcAddNewRegistration.writeData.setString(4, emailField.getText());
//                                jdbcAddNewRegistration.writeData.setString(5, EGNField.getText());
//                                jdbcAddNewRegistration.writeData.setString(6, telephoneNumberField.getText());
//                                jdbcAddNewRegistration.writeData.setString(7, positionField.getText());
//                                jdbcAddNewRegistration.writeData.executeUpdate();
//                                jdbcAddNewRegistration.writeData.close();
//                                //Adding teacher in registrations table
//
//                                //Inserting teacher into teachers table
//                                jdbcAddNewTeacher.writeData.setString(1, firstNameField.getText());
//                                jdbcAddNewTeacher.writeData.setString(2, lastNameField.getText());
//                                jdbcAddNewTeacher.writeData.setString(3, emailField.getText());
//                                jdbcAddNewTeacher.writeData.setString(4, classNumberField.getText());
//                                jdbcAddNewTeacher.writeData.executeUpdate();
//                                jdbcAddNewTeacher.writeData.close();
//                                //Inserting teacher into teachers table
//
//                                //Inserting teacher into its class section
//                                jdbcAddNewStudent.writeData.setString(1, firstNameField.getText());
//                                jdbcAddNewStudent.writeData.setString(2, lastNameField.getText());
//                                jdbcAddNewStudent.writeData.setString(3, classNumberField.getText());
//                                jdbcAddNewStudent.writeData.setString(4, positionField.getText());
//                                jdbcAddNewStudent.writeData.executeUpdate();
//                                jdbcAddNewStudent.writeData.close();
//                                //Inserting teacher into its class section
//
//                                errorLabel.setText("Added new teacher: " + firstNameField.getText() + " " + lastNameField.getText() + "\nEmail: " + emailField.getText() + "\nClass teacher of the section: " + classNumberField.getText());
//
//                            }
//                            //If in the section's SQL table there isn't a class teacher
//
//                            //If in the section's SQL table there is already a class teacher
//                            else {
//                                errorLabel.setText("This section already has a class teacher!");
//                            }
//                            //If in the section's SQL table there is already a class teacher
//
//                        }
//                        //If inserted section exist
//
//                        //If inserted section does not exist
//                        else {
//                            errorLabel.setText("Cannot create a new student in section: " + classNumberField.getText().substring(0, 3) + "\nSection does not exist!");
//                        }
//                        //If inserted section does not exist
//
//                    }
//                    //If section is inserted
//
//                    //If section is not inserted -> only '-'
//                    else {
//
//                        //If section is valid
//                        if (!classNumberField.getText().equals("-")) {
//
//                            //Adding teacher in registrations table
//                            jdbcAddNewRegistration.writeData.setString(1, classNumberField.getText());
//                            jdbcAddNewRegistration.writeData.setString(2, firstNameField.getText());
//                            jdbcAddNewRegistration.writeData.setString(3, lastNameField.getText());
//                            jdbcAddNewRegistration.writeData.setString(4, emailField.getText());
//                            jdbcAddNewRegistration.writeData.setString(5, EGNField.getText());
//                            jdbcAddNewRegistration.writeData.setString(6, telephoneNumberField.getText());
//                            jdbcAddNewRegistration.writeData.setString(7, positionField.getText());
//                            jdbcAddNewRegistration.writeData.executeUpdate();
//                            jdbcAddNewRegistration.writeData.close();
//                            //Adding teacher in registrations table
//
//                            //Inserting teacher into teachers table
//                            jdbcAddNewTeacher.writeData.setString(1, firstNameField.getText());
//                            jdbcAddNewTeacher.writeData.setString(2, lastNameField.getText());
//                            jdbcAddNewTeacher.writeData.setString(3, emailField.getText());
//                            jdbcAddNewTeacher.writeData.setString(4, classNumberField.getText());
//                            jdbcAddNewTeacher.writeData.executeUpdate();
//                            jdbcAddNewTeacher.writeData.close();
//                            //Inserting teacher into teachers table
//
//                        }
//                        //If section is valid
//
//                        //If section is invalid -> length != 3
//                        else if (classNumberField.getText().length() != 3) {
//                            errorLabel.setText("When creating new teacher section MUST be with length 3!");
//                        }
//                        //If section is invalid -> length != 3
//
//                    }
//                    //If section is not inserted -> only '-'
//
//                }
//                //If wanted to add teacher


}
