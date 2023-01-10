import { StyleSheet } from 'react-native';
import { color } from 'react-native/Libraries/Components/View/ReactNativeStyleAttributes';
export const colors = {
  primary: '#FEC62F',
  primaryLight: '#E47112',
  primaryGradientStart: '#4f44b6',
  primaryGradientEnd: '#4f44b6',
  secondaryGradientStart: '#FF1358',
  secondaryGradientEnd: '#FF1358',
  profileGradientStart: '#54CBF6',
  profileGradientEnd: '#49D2D0',
  secondary: '#FF1358',
  grey: '#acacac',
  gray: '#5f5f5f',
  darkGray: '#4d4d4d',
  lightGray: '#9b9b9b',
  white: '#ffffff',
  colorApp:'#55BCF6',
  blue: '#5A81F7',
  bluish: '#F1F1F7',
  black: '#000000',
  green: '#6DD0A3',
  yellow: '#ffc247',
  red: '#f00',
  maroon: '#800',
  redLight : '#FBAA9C'
};
export const GlobalStyles = StyleSheet.create({
  containerLogin:{
    flex: 1,
    padding: 20,
    width: '100%',
    maxWidth: 340,
    alignSelf: 'center',
    alignItems: 'center',
    justifyContent: 'center'
  },
  itemTransfer: {
    backgroundColor:colors.primary,
    padding: 5,
    marginVertical: 2,
    marginHorizontal: 10,
  },
  titleTransfer: {
    fontSize: 13,
  },
  txt:{
    marginTop:10,
    fontSize:16,
    fontWeight:'bold',
    color:colors.primary
  },
  addBenScreen:{
    flex: 1,
    padding: 20,
    width: '100%',
    maxWidth: 340,
  },
    container: {
      flex: 1,
      backgroundColor: '#271D1D',
    },
    transfersWrapper: {
      paddingTop:10,
      paddingHorizontal: 20,
    },
    sectionTitle: {
      fontSize: 20,
      marginTop:36,
      marginLeft:70,
      fontWeight: 'bold'
    },
    items: {
      marginTop: 20,
    },
    input: {
      paddingVertical: 15,
      paddingHorizontal: 15,
      backgroundColor: '#FFF',
      borderRadius: 60,
      borderColor: '#C0C0C0',
      borderWidth: 1,
      width: 250,
    },
    addWrapper: {
      width: 60,
      height: 60,
      backgroundColor: '#FFF',
      borderRadius: 60,
      justifyContent: 'center',
      alignItems: 'center',
      borderColor: '#C0C0C0',
      borderWidth: 1,
    },
    addText: {},
    item: {
        backgroundColor:colors.primary,
        padding: 15,
        borderRadius: 10,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        marginBottom: 20,
    },
      itemLeft: {
        flexDirection: 'row',
        alignItems: 'center',
        flexWrap: 'wrap'
      },
      square: {
        width: 24,
        height: 24,
        opacity: 0.4,
        borderRadius: 5,
        marginRight: 15,
      },
      itemText: {
        color:colors.white,
        maxWidth: '100%',
      },
      itemText2: {
        maxWidth: '100%',
        color:colors.colorApp
      },
      circular: {
       
      },
      typeTransfer:{
        width: 30,
        height: 30
      }

  });
  