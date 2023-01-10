import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity,Image} from 'react-native';
import { GlobalStyles } from '../styles/globalStyles';
const Transfer = (props) => {

  return (
    <View style={GlobalStyles.item}>
      <View style={GlobalStyles.itemLeft}>
        <View style={GlobalStyles.square}>
          
        </View>
        <Text style={GlobalStyles.itemText}>{props.text}</Text>
      </View>
      <View style={GlobalStyles.circular}>
        <Image source={require('../assets/logo.png')} style={GlobalStyles.typeTransfer}/>
      </View>
    </View>
  )
}

export default Transfer;