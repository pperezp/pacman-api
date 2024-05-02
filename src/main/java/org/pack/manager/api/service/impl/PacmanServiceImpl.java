package org.pack.manager.api.service.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.pack.manager.api.mapper.LitePackageMapper;
import org.pack.manager.api.mapper.PackageMapper;
import org.pack.manager.api.mapper.UpgradePackageMapper;
import org.pack.manager.api.model.*;
import org.pack.manager.api.model.Package;
import org.pack.manager.api.service.CommandRunner;
import org.pack.manager.api.service.PackageService;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Slf4j
@Service
@AllArgsConstructor
public class PacmanServiceImpl implements PackageService {

    private final CommandRunner commandRunner;
    private final PackageMapper packageMapper;
    private final UpgradePackageMapper upgradePackageMapper;
    private final LitePackageMapper litePackageMapper;

    @Override
    public List<Package> getExplicitInstalledPackages() {
        CommandRequest commandRequest = new CommandRequest("pacman -Qnei");
        CommandResult commandResult = commandRunner.exec(commandRequest);

        List<String> output = commandResult.getOutput();

        return packageMapper.map(output);
    }

    @Override
    public List<LitePackage> getExplicitLiteInstalledPackages() {
        CommandRequest commandRequest = new CommandRequest("pacman -Qne");
        CommandResult commandResult = commandRunner.exec(commandRequest);

        List<String> output = commandResult.getOutput();

        return litePackageMapper.map(output);
    }

    @Override
    public List<UpgradePackage> getUpgradePackages(String rootPassword) {
        CommandRequest commandRequest = new CommandRequest("pacman -Sy", rootPassword);
        CommandResult commandResult = commandRunner.exec(commandRequest);

        log.info("pacman -Sy success?: {}", commandResult.isSuccess());

        commandRequest = new CommandRequest("pacman -Qu");
        commandResult = commandRunner.exec(commandRequest);

        List<String> output = commandResult.getOutput();

        return upgradePackageMapper.map(output);
    }

    @Override
    public Package getPackageBy(String name) {
        CommandRequest commandRequest = new CommandRequest("pacman -Qni " + name);
        CommandResult commandResult = commandRunner.exec(commandRequest);

        List<String> output = commandResult.getOutput();

        return packageMapper.mapToOne(output);
    }

}
