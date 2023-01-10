export function passwordValidator(password) {
  if (!password) return "Password can't be empty."
  if (password.length < 3) return 'Password must be at least 4 characters long.'
  return ''
}
