export function phoneValidator(phone) {
    const re = /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im
    if (!phone) return "Phone can't be empty."
    if (!re.test(phone)) return 'Ooops! We need a valid number phone.'
    return ''
  }
  