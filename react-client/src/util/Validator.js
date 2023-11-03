export function checkLength(value, size) {
  return value.length < size;
}
export function checkPasswords(pass1, pass2) {
  return pass1 === pass2;
}
export function isValidEmail(email) {
  const emailRegex = /^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
  return emailRegex.test(email);
}
