import React from 'react'
import { TouchableOpacity, Image, StyleSheet } from 'react-native'
import { MaterialIcons } from '@expo/vector-icons';
import { colors } from '../styles/globalStyles';

export default function Menu({navigation}) {
  const openMenu = () => {
    navigation.openDrawer();
  }
  return (
    <TouchableOpacity onPress={openMenu} style={styles.menuContainer}>
        <MaterialIcons name='menu' color={colors.colorApp} size={25} style={styles.icon} />
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  menuContainer:{
    borderRadius:10,
    borderWidth:1,
    marginTop:30,
    borderColor:colors.grey,
    position: 'absolute',
    left: 16,
    padding:7
  },
  icon: {
    
  }
});