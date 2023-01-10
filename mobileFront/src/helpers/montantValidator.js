export function montantValidator(montant){
    const re = /^\d*\.?\d+$/
    if (!montant) return "Amount can't be empty."
    if (!re.test(montant)) return 'Ooops! We need a valid amount.'
    return ''

}