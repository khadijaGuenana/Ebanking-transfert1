import React from 'react'
import { StyleSheet } from 'react-native'
import { Text } from 'react-native-paper'
import { theme } from '../core/theme'
import { colors } from '../styles/globalStyles'

export default function Header(props) {
  return <Text style={styles.header} {...props} />
}

const styles = StyleSheet.create({
  header: {
    fontSize: 21,
    color:colors.colorApp,
    fontWeight: 'bold',
    paddingVertical: 12,
  },
})
