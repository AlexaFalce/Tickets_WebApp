const nameField = document.getElementById('nameForm');
let isNameValid = false;
const surnameField = document.getElementById('surnameForm');
let isSurnameValid = false;
const usernameField = document.getElementById('usernameForm');
let isUsernameValid = false;
const passwordField = document.getElementById('passwordForm');
let isPasswordValid = false;
const confirmPasswordField = document.getElementById('confirmPasswordForm');
let arePasswordsMatching = false;
const submitContainer = document.getElementById("submitContainer");


function isLetterUppercaseAndLowercase(field) {
    return /^[A-Za-z]+$/.test(field);
}

function isLetterUppercaseLowercaseAndUnderscore(field) {
    return /^[A-Za-z0-9_]+$/.test(field);
}

function checkLength(field, min, max) {
    return field.value.length <= max && field.value.length >= min;
}

function isEqual(original, check) {
    return original === check;
}


// change style of the form
function validateField(field, isValid) {
    if (isValid) {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');
    } else {
        field.classList.remove('is-valid');
        field.classList.add('is-invalid');
    }
}

// show/hide button create if fields are correct/wrong
function updateSubmitButton() {
    if (isNameValid && isSurnameValid && isUsernameValid && isPasswordValid && arePasswordsMatching) {
        if (submitContainer.children.length > 0)  return;
        let button = document.createElement("button");
        button.id = "submitButton";
        button.type = "submit";
        button.textContent = "Create";
        button.classList.add("btn", "btn-outline-primary", "col-2");
        submitContainer.appendChild(button);
    } else {
        while (submitContainer.firstChild) submitContainer.firstChild.remove();
    }
}


// check if name is correct
nameField.addEventListener('input', function() {
    isNameValid = isLetterUppercaseAndLowercase(nameField.value);
    validateField(nameField, isNameValid)
    updateSubmitButton(isNameValid);
});

// check if surname is correct
surnameField.addEventListener('input', function() {
    isSurnameValid = isLetterUppercaseAndLowercase(surnameField.value);
    validateField(surnameField, isSurnameValid);
    updateSubmitButton(isSurnameValid);
});

// check if username is correct
usernameField.addEventListener('input', function() {
    isUsernameValid = isLetterUppercaseLowercaseAndUnderscore(usernameField.value) && checkLength(usernameField, 1, 15);
    validateField(usernameField, isUsernameValid);
    updateSubmitButton(isUsernameValid);
});

// check if password is correct
passwordField.addEventListener('input', function() {
    isPasswordValid = isLetterUppercaseLowercaseAndUnderscore(passwordField.value) && checkLength(passwordField, 8, 15);
    validateField(passwordField, isPasswordValid);
    arePasswordsMatching = isEqual(passwordField.value, confirmPasswordField.value);
    validateField(confirmPasswordField, arePasswordsMatching);
    updateSubmitButton(isPasswordValid);
});

// check if confrim password is correct
confirmPasswordField.addEventListener('input', function() {
    isPasswordValid = isLetterUppercaseLowercaseAndUnderscore(passwordField.value) && checkLength(passwordField, 8, 15);
    arePasswordsMatching = isEqual(passwordField.value, confirmPasswordField.value);
    validateField(confirmPasswordField, arePasswordsMatching);
    updateSubmitButton(arePasswordsMatching);
});
