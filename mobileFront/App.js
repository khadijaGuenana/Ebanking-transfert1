import 'react-native-gesture-handler';
import React, {useEffect } from 'react';
import {View,TouchableOpacity,Image} from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { createDrawerNavigator } from '@react-navigation/drawer';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import Dashboard from './src/screens/Dashboard';
import LoginScreen from './src/screens/LoginScreen'
import AddTransferScreen from './src/screens/addTransfer'
import TransferDetailsScreen from './src/screens/TransferDetails';
import { colors } from './src/styles/globalStyles';
import ActivityIndicator from './src/styles/ActivityIndicator';
import AddBeneficiaryScreen from './src/screens/AddBeneficiary';
import { DrawerContent } from './src/utils/DrawerContent';

const Stack = createStackNavigator();
const Drawer = createDrawerNavigator();

const NavigationDrawerStructure = (props)=> {
  //Structure for the navigatin Drawer
  const toggleDrawer = () => {
    //Props to open/close the drawer
    props.navigationProps.toggleDrawer();
  };

  return (
    <View style={{ flexDirection: 'row' }}>
      <TouchableOpacity onPress={()=> toggleDrawer()}>
        {/*Donute Button Image */}
        <Image
          source={{uri: 'https://raw.githubusercontent.com/AboutReact/sampleresource/master/drawerWhite.png'}}
          style={{
            width: 25,
            height: 25,
            marginLeft: 5,
            color:colors.colorApp
          }}
        />
       
      </TouchableOpacity>
    </View>
  );
}
function loginScreenStack({ navigation }) {
  return (
      <Stack.Navigator initialRouteName="LoginScreen"
                  screenOptions={{
                    headerShown:false
                }}
                  
      >
        <Stack.Screen
          name="LoginScreen"
          component={LoginScreen}
        />
      </Stack.Navigator>
  );
}

function dashboardScreenStack({ navigation }) {
    return (
      <Stack.Navigator
        initialRouteName="Dashboard"
        screenOptions={{
          headerShown:false
      }}
        >
        <Stack.Screen
          name="DashboardScreen"
          component={Dashboard}
          />
          <Stack.Screen
          name="AddTransferScreen"
          component={AddTransferScreenStack}
          />
           <Stack.Screen
          name="TransferDetailsScreen"
          component={TransferDetailsScreen}
          />
      </Stack.Navigator>
    );
}
function AddTransferScreenStack({navigation}){
  return(
    <Stack.Navigator
      initialRouteName="AddTransferScreen"
      screenOptions={{
        headerShown:false
    }}
      >
       <Stack.Screen
        name="AddTransferScreen"
        component={AddTransferScreen}
        />
        <Stack.Screen
        name="AddBeneficiaryScreen"
        component={AddBeneficiaryScreen}
        />
    </Stack.Navigator>
  );
}
function App() {
    return (
      <>
          <NavigationContainer>
            <Drawer.Navigator
              drawerContent={props => <DrawerContent {...props} />}
              >
                <Drawer.Screen
                    name="Login"
                    component={loginScreenStack}  
                  />
                  <Drawer.Screen
                    name="Dashboard"
                    component={dashboardScreenStack} 
                />
              </Drawer.Navigator>
        </NavigationContainer>
      </>
    );
  }
  
  export default App;