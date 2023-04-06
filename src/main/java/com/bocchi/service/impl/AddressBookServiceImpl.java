package com.bocchi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bocchi.mapper.AddressBookMapper;
import com.bocchi.pojo.AddressBook;
import com.bocchi.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
