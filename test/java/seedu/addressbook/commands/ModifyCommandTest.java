package seedu.addressbook.commands;

import org.junit.Before;
import org.junit.Test;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.ui.TextUi;
import seedu.addressbook.util.TestUtil;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class ModifyCommandTest {
    private AddressBook emptyAddressBook;
    private AddressBook addressBook;

    private List<ReadOnlyPerson> emptyDisplayList;
    private List<ReadOnlyPerson> originalList;
    private List<ReadOnlyPerson> modifiedList;

    @Before
    public void setUp() throws Exception {
        Person johnDoe = new Person(new Name("John Doe"), new Phone("61234567", false),
                new Email("john@doe.com", false), new Address("395C Ben Road", false), Collections.emptySet());
        Person janeDoe = new Person(new Name("Jane Doe"), new Phone("91234567", false),
                new Email("jane@doe.com", false), new Address("33G Ohm Road", false), Collections.emptySet());
        Person samDoe = new Person(new Name("Sam Doe"), new Phone("63345566", false),
                new Email("sam@doe.com", false), new Address("55G Abc Road", false), Collections.emptySet());
        Person davidGrant = new Person(new Name("David Grant"), new Phone("61121122", false),
                new Email("david@grant.com", false), new Address("44H Define Road", false),
                Collections.emptySet());

        emptyAddressBook = TestUtil.createAddressBook();
        addressBook = TestUtil.createAddressBook(johnDoe, janeDoe, davidGrant, samDoe);

        emptyDisplayList = TestUtil.createList();

        originalList = TestUtil.createList(johnDoe, janeDoe, samDoe, davidGrant);
    }

    @Test
    public void execute_noPersonDisplayed_returnsInvalidIndexMessage() {
        Person Test = TestUtil.generateTestPerson();
        assertModifyFailsDueToInvalidIndex(1 ,Test,addressBook, emptyDisplayList);
    }

    @Test
    public void execute_invalidIndex_returnsInvalidIndexMessage() {
        Person Test = TestUtil.generateTestPerson();
        assertModifyFailsDueToInvalidIndex(0, Test, addressBook, originalList);
        assertModifyFailsDueToInvalidIndex(-1, Test, addressBook, originalList);
        assertModifyFailsDueToInvalidIndex(originalList.size() + 1, Test, addressBook, originalList);
    }

    @Test
    public void execute_validModification_returnsSuccessMessage() throws UniquePersonList.PersonNotFoundException,UniquePersonList.DuplicatePersonException {
        Person Test = TestUtil.generateTestPerson();
        assertModifySuccessful(1,Test,addressBook,originalList);
    }
    /**
     * Creates new modify command
     * @param targetVisibleIndex index of the person to be modified
     * @param person new details to replace the target's details
     */
    private ModifyCommand createModifyCommand(int targetVisibleIndex, Person person, AddressBook addressBook,
                                              List<ReadOnlyPerson> displayList) {

        ModifyCommand command = new ModifyCommand(targetVisibleIndex,person);
        command.setData(addressBook, displayList);

        return command;
    }

    /**
     * Executes the command, and checks that the valid behavior is as expect
     */
    private void assertValidBehaviour(ModifyCommand modifyCommand, String expectedMessage,
                                        AddressBook expectedAddressBook, AddressBook actualAddressBook) {

        CommandResult result = modifyCommand.execute();
        assertEquals(expectedMessage, result.feedbackToUser);
        assertNotEquals(expectedAddressBook.getAllPersons(), actualAddressBook.getAllPersons());
    }

    /**
     * Executes the command, and checks that the invalid behavior is as expect
     */
    private void assertInvalidBehaviour(ModifyCommand modifyCommand, String expectedMessage,
                                      AddressBook expectedAddressBook, AddressBook actualAddressBook) {

        CommandResult result = modifyCommand.execute();
        assertEquals(expectedMessage, result.feedbackToUser);
    }

    /**
     * Asserts that the index is not valid for the given display list.
     */
    private void assertModifyFailsDueToInvalidIndex(int invalidVisibleIndex,Person person, AddressBook addressBook,
                                                      List<ReadOnlyPerson> displayList) {

        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

        ModifyCommand command = createModifyCommand(invalidVisibleIndex, person, addressBook, displayList);
        assertInvalidBehaviour(command, expectedMessage, addressBook, addressBook);
    }


    /**
     * Asserts that the person at the specified index can be successfully modified
     *
     * The addressBook passed in will not be modified (no side effects).
     *
     * @throws UniquePersonList.PersonNotFoundException if the selected person is not in the address book
     */
    private void assertModifySuccessful(int targetVisibleIndex, Person person, AddressBook addressBook,
                                          List<ReadOnlyPerson> displayList) throws UniquePersonList.PersonNotFoundException,UniquePersonList.DuplicatePersonException {

        ReadOnlyPerson targetPerson = displayList.get(targetVisibleIndex - TextUi.DISPLAYED_INDEX_OFFSET);

        AddressBook expectedAddressBook = TestUtil.clone(addressBook);
        expectedAddressBook.modifyPerson(targetPerson,person);
        String expectedMessage = String.format(ModifyCommand.MESSAGE_MODIFY_SUCCESS, targetPerson,person);

        AddressBook actualAddressBook = TestUtil.clone(addressBook);

        ModifyCommand command = createModifyCommand(targetVisibleIndex, person, addressBook, displayList);
        assertValidBehaviour(command, expectedMessage, expectedAddressBook, actualAddressBook);
    }
}
