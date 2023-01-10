import React, { useEffect, useState } from 'react';
import { View, StyleSheet } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {Avatar,Title,Drawer} from 'react-native-paper';
import {
    DrawerContentScrollView,
    DrawerItem
} from '@react-navigation/drawer';

import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import Ionicons from 'react-native-vector-icons/Ionicons';
import { theme } from '../core/theme';
import { TouchableOpacity } from 'react-native-gesture-handler';


export function DrawerContent(props) {
    const [user,setUser] = useState({})
    const [isLogin,setIsLogin] = useState(false)
    useEffect(() => {
        setTimeout(async() => {
          let user = null;
          try {
            user  = await AsyncStorage.getItem('user');
            if(user!=null) {
                user = JSON.parse(user);
                setUser(user);
                setIsLogin(true)
            }
          } catch(e) {
            console.log(e);
          }
        }, 1000);
      });
    const goLogin=()=>{
        props.navigation.navigate('Login');
    }
    const logOut=async()=>{
        await AsyncStorage.removeItem('user')
        setUser({})
        await setIsLogin(false)
        props.navigation.navigate('Login')
    }
      return (
        <View style={{flex:1}}>
        <DrawerContentScrollView {...props}>
            {
                isLogin == false ?
                (
                    <Drawer.Section style={styles.drawerSection} >
                        <DrawerItem
                            onPress={()=>goLogin()}
                            icon={({color, size}) => (
                                <Ionicons
                                name="log-in-outline"
                                color={color}
                                size={size}
                                />
                            )}
                            label="Se connecter"
                        />

                    </Drawer.Section>
                )
                :
                (
                    <View style={styles.drawerContent}>
                        <View style={styles.userInfoSection}>
                            <TouchableOpacity style={{flexDirection:'row',marginTop: 15 , alignItems:'center'}}>
                                <Avatar.Image
                                    source={require('../assets/logo.png')}
                                    size={50}
                                />
                                <View style={{marginLeft:15, alignItems:'center'}}>
                                    <Title style={styles.title}>Bank Of Affrica</Title>
                                </View>
                            </TouchableOpacity>
                        </View>

                        <Drawer.Section style={styles.drawerSection}>
                            <DrawerItem
                                icon={({color, size}) => (
                                    <Icon
                                        name="home-outline"
                                        color={color}
                                        size={size}
                                    />
                                )}
                                label="Dashboard"
                                onPress={() => {props.navigation.navigate('DashboardScreen')
                                }}
                            />
                        </Drawer.Section>
                    </View>
                )
            }
        </DrawerContentScrollView>
        {
            isLogin==false?
            (
                <View></View>
            )
            :
            (
                <Drawer.Section style={styles.bottomDrawerSection} >
                    <DrawerItem
                        onPress={()=>logOut()}
                        icon={({color, size}) => (
                            <Icon
                            name="exit-to-app"
                            color={color}
                            size={size}
                            />
                        )}
                        label="Déconnexion"
                    />
                </Drawer.Section>
            )
        }
    </View>
      )



}

const styles = StyleSheet.create({
    drawerContent: {
      flex: 1,
    },
    userInfoSection: {
      paddingLeft: 20,
    },
    title: {
      fontSize: 16,
      marginTop: 3,
    },
    caption: {
      fontSize: 14,
      lineHeight: 14,
    },
    row: {
      marginTop: 20,
      flexDirection: 'row',
      alignItems: 'center',
    },
    section: {
      flexDirection: 'row',
      alignItems: 'center',
      marginRight: 15,
    },
    paragraph: {
        marginRight: 3,
    },
    drawerSection: {
      marginTop: 15,
    },
    bottomDrawerSection: {
        marginBottom: 15,
        borderTopColor: '#f4f4f4',
        borderTopWidth: 1
    },
    preference: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      paddingVertical: 12,
      paddingHorizontal: 16,
    },
    active :{
        backgroundColor : theme.colors.secondary,
    }
  });
